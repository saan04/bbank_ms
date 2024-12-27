package com.bloodbank.inventory.controller;

import com.bloodbank.inventory.model.BloodInventory;
import com.bloodbank.inventory.model.BloodTransaction;
import com.bloodbank.inventory.service.BloodInventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class BloodInventoryController {

    @Autowired
    private BloodInventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<BloodInventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{bloodGroup}")
    public ResponseEntity<BloodInventory> getInventoryByBloodGroup(@PathVariable("bloodGroup") String bloodGroup) {
        return ResponseEntity.ok(inventoryService.getInventoryByBloodGroup(bloodGroup));
    }

    @PostMapping
    public ResponseEntity<BloodInventory> addBloodGroup(@Valid @RequestBody BloodInventory inventory) {
        return new ResponseEntity<>(inventoryService.addBloodGroup(inventory), HttpStatus.CREATED);
    }

    @PostMapping("/{bloodGroup}/donate")
    public ResponseEntity<BloodInventory> addDonation(
            @PathVariable("bloodGroup") String bloodGroup,
            @RequestBody Map<String, Integer> request) {
        return ResponseEntity.ok(inventoryService.updateInventory(
                bloodGroup,
                request.get("quantity"),
                BloodTransaction.TransactionType.DONATION));
    }

    @PostMapping("/{bloodGroup}/request")
    public ResponseEntity<BloodInventory> processRequest(
            @PathVariable("bloodGroup") String bloodGroup,
            @RequestBody Map<String, Integer> request) {
        return ResponseEntity.ok(inventoryService.updateInventory(
                bloodGroup,
                request.get("quantity"),
                BloodTransaction.TransactionType.REQUEST));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<BloodTransaction>> getTransactionHistory() {
        return ResponseEntity.ok(inventoryService.getTransactionHistory());
    }

    @GetMapping("/transactions/date-range")
    public ResponseEntity<List<BloodTransaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(inventoryService.getTransactionsByDateRange(start, end));
    }

    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<List<BloodTransaction>> getTransactionsByType(
            @PathVariable("type") BloodTransaction.TransactionType type) {
        return ResponseEntity.ok(inventoryService.getTransactionsByType(type));
    }

    @GetMapping("/{bloodGroup}/check")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable("bloodGroup") String bloodGroup,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.checkAvailability(bloodGroup, quantity));
    }

    @PostMapping("/check-low-inventory")
    public ResponseEntity<Void> checkLowInventory() {
        inventoryService.checkLowInventory();
        return ResponseEntity.ok().build();
    }
}
