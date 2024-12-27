package com.bloodbank.donor.controller;

import com.bloodbank.donor.model.Donor;
import com.bloodbank.donor.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorService donorService;

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<Donor>> getDonorsByBloodGroup(@PathVariable("bloodGroup") String bloodGroup) {
        return ResponseEntity.ok(donorService.getDonorsByBloodGroup(bloodGroup));
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<Donor>> getEligibleDonors() {
        return ResponseEntity.ok(donorService.getEligibleDonors());
    }

    @PostMapping
    public ResponseEntity<Donor> createDonor(@Valid @RequestBody Donor donor) {
        return new ResponseEntity<>(donorService.createDonor(donor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable("id") Long id, @Valid @RequestBody Donor donor) {
        return ResponseEntity.ok(donorService.updateDonor(id, donor));
    }

    @PostMapping("/{id}/donate")
    public ResponseEntity<Void> recordDonation(@PathVariable("id") Long id) {
        donorService.recordDonation(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable("id") Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.noContent().build();
    }
}
