package com.bloodbank.donation.service;

import com.bloodbank.donation.dto.DonationRequest;
import com.bloodbank.donation.model.BloodDonation;
import com.bloodbank.donation.model.DonationStatus;
import com.bloodbank.donation.repository.BloodDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodDonationService {
    
    @Autowired
    private BloodDonationRepository donationRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    public BloodDonation initiateDonation(DonationRequest request) {
        // Check donor eligibility
        Boolean isEligible = restTemplate.getForObject(
            "http://localhost:8081/api/donors/" + request.getDonorId() + "/eligible",
            Boolean.class
        );

        if (Boolean.FALSE.equals(isEligible)) {
            throw new RuntimeException("Donor is not eligible for donation at this time");
        }

        BloodDonation donation = new BloodDonation();
        donation.setDonorId(request.getDonorId());
        donation.setBloodType(request.getBloodType());
        donation.setQuantity(request.getQuantity());
        donation.setLocation(request.getLocation());
        donation.setNotes(request.getNotes());
        donation.setDonationDate(LocalDateTime.now());
        donation.setStatus(DonationStatus.PENDING);
        donation.setHasMedicalClearance(false);

        return donationRepository.save(donation);
    }

    public BloodDonation updateDonationStatus(Long donationId, DonationStatus status, String screeningNotes) {
        BloodDonation donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new RuntimeException("Donation not found"));

        donation.setStatus(status);
        donation.setScreeningNotes(screeningNotes);

        if (status == DonationStatus.COMPLETED) {
            donation.setHasMedicalClearance(true);
            // Notify inventory service about new blood donation
            notifyInventoryService(donation);
            // Update donor's donation status
            updateDonorStatus(donation.getDonorId());
        }

        return donationRepository.save(donation);
    }

    private void notifyInventoryService(BloodDonation donation) {
        // TODO: Implement inventory service notification
        // This will be implemented when we create the inventory service
    }

    private void updateDonorStatus(Long donorId) {
        restTemplate.put("http://localhost:8081/api/donors/" + donorId + "/donation", null);
    }

    public List<BloodDonation> getDonationsByDonor(Long donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    public List<BloodDonation> getDonationsByBloodType(String bloodType) {
        return donationRepository.findByBloodType(bloodType);
    }

    public List<BloodDonation> getDonationsByStatus(DonationStatus status) {
        return donationRepository.findByStatus(status);
    }

    public List<BloodDonation> getDonationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return donationRepository.findByDonationDateBetween(startDate, endDate);
    }
}
