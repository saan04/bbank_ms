package com.bloodbank.donor.service;

import com.bloodbank.donor.model.Donor;
import com.bloodbank.donor.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    
    @Autowired
    private DonorRepository donorRepository;

    public Donor registerDonor(Donor donor) {
        donor.setEligible(true);
        donor.setDonationCount(0);
        return donorRepository.save(donor);
    }

    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public Donor updateDonor(Long id, Donor donorDetails) {
        Donor donor = donorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        donor.setName(donorDetails.getName());
        donor.setEmail(donorDetails.getEmail());
        donor.setPhoneNumber(donorDetails.getPhoneNumber());
        
        return donorRepository.save(donor);
    }

    public void updateDonationStatus(Long id) {
        Donor donor = donorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        donor.setLastDonationDate(LocalDateTime.now());
        donor.setDonationCount(donor.getDonationCount() + 1);
        donor.setEligible(false); // Assuming 3 months cooling period
        
        donorRepository.save(donor);
    }

    public boolean isDonorEligible(Long id) {
        Donor donor = donorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        if (donor.getLastDonationDate() == null) {
            return true;
        }

        // Check if 3 months have passed since last donation
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return donor.getLastDonationDate().isBefore(threeMonthsAgo);
    }
}
