package com.bloodbank.donation.controller;

import com.bloodbank.donation.dto.DonationRequest;
import com.bloodbank.donation.model.BloodDonation;
import com.bloodbank.donation.model.DonationStatus;
import com.bloodbank.donation.service.BloodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class BloodDonationController {
    
    @Autowired
    private BloodDonationService donationService;

    @PostMapping
    public ResponseEntity<BloodDonation> initiateDonation(@RequestBody DonationRequest request) {
        return ResponseEntity.ok(donationService.initiateDonation(request));
    }

    @PutMapping("/{donationId}/status")
    public ResponseEntity<BloodDonation> updateDonationStatus(
            @PathVariable Long donationId,
            @RequestParam DonationStatus status,
            @RequestParam(required = false) String screeningNotes) {
        return ResponseEntity.ok(donationService.updateDonationStatus(donationId, status, screeningNotes));
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<BloodDonation>> getDonationsByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(donationService.getDonationsByDonor(donorId));
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<BloodDonation>> getDonationsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(donationService.getDonationsByBloodType(bloodType));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodDonation>> getDonationsByStatus(@PathVariable DonationStatus status) {
        return ResponseEntity.ok(donationService.getDonationsByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<BloodDonation>> getDonationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(donationService.getDonationsByDateRange(startDate, endDate));
    }
}
