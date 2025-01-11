package com.bloodbank.donor.controller;

import com.bloodbank.donor.model.Donor;
import com.bloodbank.donor.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
    
    @Autowired
    private DonorService donorService;

    @PostMapping
    public ResponseEntity<Donor> registerDonor(@RequestBody Donor donor) {
        return ResponseEntity.ok(donorService.registerDonor(donor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonor(@PathVariable Long id) {
        return donorService.getDonorById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable Long id, @RequestBody Donor donor) {
        return ResponseEntity.ok(donorService.updateDonor(id, donor));
    }

    @PutMapping("/{id}/donation")
    public ResponseEntity<Void> updateDonationStatus(@PathVariable Long id) {
        donorService.updateDonationStatus(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/eligible")
    public ResponseEntity<Boolean> checkDonorEligibility(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.isDonorEligible(id));
    }
}
