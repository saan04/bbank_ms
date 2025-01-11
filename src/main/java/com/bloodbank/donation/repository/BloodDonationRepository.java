package com.bloodbank.donation.repository;

import com.bloodbank.donation.model.BloodDonation;
import com.bloodbank.donation.model.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long> {
    List<BloodDonation> findByDonorId(Long donorId);
    List<BloodDonation> findByBloodType(String bloodType);
    List<BloodDonation> findByStatus(DonationStatus status);
    List<BloodDonation> findByDonationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<BloodDonation> findByLocationAndStatus(String location, DonationStatus status);
}
