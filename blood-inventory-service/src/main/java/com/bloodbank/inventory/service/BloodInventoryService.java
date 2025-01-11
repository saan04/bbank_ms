package com.bloodbank.inventory.service;

import com.bloodbank.inventory.model.*;
import com.bloodbank.inventory.repository.BloodInventoryRepository;
import com.bloodbank.inventory.repository.BloodTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodInventoryService {
    
    @Autowired
    private BloodInventoryRepository inventoryRepository;
    
    @Autowired
    private BloodTransactionRepository transactionRepository;

    @Transactional
    public BloodInventory addBloodDonation(String bloodType, Double quantity, String location, String donationId) {
        BloodInventory inventory = inventoryRepository.findByBloodTypeAndLocation(bloodType, location)
            .orElseGet(() -> createNewInventory(bloodType, location));

        inventory.setAvailableUnits(inventory.getAvailableUnits() + quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        updateInventoryStatus(inventory);

        // Record transaction
        BloodTransaction transaction = new BloodTransaction();
        transaction.setBloodType(bloodType);
        transaction.setQuantity(quantity);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType(TransactionType.DONATION_IN);
        transaction.setReferenceId(donationId);
        transaction.setLocation(location);
        transactionRepository.save(transaction);

        return inventoryRepository.save(inventory);
    }

    @Transactional
    public BloodInventory requestBlood(String bloodType, Double quantity, String location, String requestId) {
        BloodInventory inventory = inventoryRepository.findByBloodTypeAndLocation(bloodType, location)
            .orElseThrow(() -> new RuntimeException("Blood type not available at specified location"));

        if (inventory.getAvailableUnits() < quantity) {
            throw new RuntimeException("Insufficient blood units available");
        }

        inventory.setAvailableUnits(inventory.getAvailableUnits() - quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        updateInventoryStatus(inventory);

        // Record transaction
        BloodTransaction transaction = new BloodTransaction();
        transaction.setBloodType(bloodType);
        transaction.setQuantity(quantity);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType(TransactionType.REQUEST_OUT);
        transaction.setReferenceId(requestId);
        transaction.setLocation(location);
        transactionRepository.save(transaction);

        return inventoryRepository.save(inventory);
    }

    private BloodInventory createNewInventory(String bloodType, String location) {
        BloodInventory inventory = new BloodInventory();
        inventory.setBloodType(bloodType);
        inventory.setLocation(location);
        inventory.setAvailableUnits(0.0);
        inventory.setReservedUnits(0.0);
        inventory.setMinimumThreshold(10.0); // Default values
        inventory.setMaximumThreshold(100.0);
        inventory.setStatus(InventoryStatus.CRITICAL);
        inventory.setLastUpdated(LocalDateTime.now());
        return inventory;
    }

    private void updateInventoryStatus(BloodInventory inventory) {
        double available = inventory.getAvailableUnits();
        double min = inventory.getMinimumThreshold();
        double max = inventory.getMaximumThreshold();

        if (available <= min * 0.5) {
            inventory.setStatus(InventoryStatus.CRITICAL);
        } else if (available <= min) {
            inventory.setStatus(InventoryStatus.LOW);
        } else if (available >= max) {
            inventory.setStatus(InventoryStatus.EXCESS);
        } else {
            inventory.setStatus(InventoryStatus.SUFFICIENT);
        }
    }

    public List<BloodInventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public BloodInventory getInventoryByBloodType(String bloodType) {
        return inventoryRepository.findByBloodType(bloodType)
            .orElse(createNewInventory(bloodType, "Central Blood Bank"));
    }

    public List<BloodInventory> getInventoryByStatus(InventoryStatus status) {
        return inventoryRepository.findByStatus(status);
    }

    public List<BloodTransaction> getTransactionHistory(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    @Transactional
    public BloodInventory updateThresholds(String bloodType, Double minimumThreshold, Double maximumThreshold) {
        BloodInventory inventory = inventoryRepository.findByBloodType(bloodType)
            .orElseThrow(() -> new RuntimeException("Blood type not found in inventory"));

        inventory.setMinimumThreshold(minimumThreshold);
        inventory.setMaximumThreshold(maximumThreshold);
        updateInventoryStatus(inventory);

        return inventoryRepository.save(inventory);
    }
}
