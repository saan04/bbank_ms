package com.bloodbank.inventory.controller;

import com.bloodbank.inventory.model.BloodInventory;
import com.bloodbank.inventory.model.BloodTransaction;
import com.bloodbank.inventory.model.InventoryStatus;
import com.bloodbank.inventory.service.BloodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class BloodInventoryController {
    
    @Autowired
    private BloodInventoryService inventoryService;

    @PostMapping("/donation")
    public ResponseEntity<BloodInventory> addBloodDonation(
            @RequestParam String bloodType,
            @RequestParam Double quantity,
            @RequestParam String location,
            @RequestParam String donationId) {
        return ResponseEntity.ok(inventoryService.addBloodDonation(bloodType, quantity, location, donationId));
    }

    @PostMapping("/request")
    public ResponseEntity<BloodInventory> requestBlood(
            @RequestParam String bloodType,
            @RequestParam Double quantity,
            @RequestParam String location,
            @RequestParam String requestId) {
        return ResponseEntity.ok(inventoryService.requestBlood(bloodType, quantity, location, requestId));
    }

    @GetMapping
    public ResponseEntity<List<BloodInventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<BloodInventory> getInventoryByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(inventoryService.getInventoryByBloodType(bloodType));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodInventory>> getInventoryByStatus(@PathVariable InventoryStatus status) {
        return ResponseEntity.ok(inventoryService.getInventoryByStatus(status));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<BloodTransaction>> getTransactionHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(inventoryService.getTransactionHistory(startDate, endDate));
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam String bloodType,
            @RequestParam Double quantity) {
        BloodInventory inventory = inventoryService.getInventoryByBloodType(bloodType);
        return ResponseEntity.ok(inventory != null && inventory.getAvailableUnits() >= quantity);
    }

    @PutMapping("/thresholds/{bloodType}")
    public ResponseEntity<BloodInventory> updateThresholds(
            @PathVariable String bloodType,
            @RequestParam Double minimumThreshold,
            @RequestParam Double maximumThreshold) {
        return ResponseEntity.ok(inventoryService.updateThresholds(bloodType, minimumThreshold, maximumThreshold));
    }
}
