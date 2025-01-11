package com.bloodbank.donation.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class BloodDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long donorId;
    private String bloodType;
    private Double quantity; // in units
    private LocalDateTime donationDate;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status;
    
    private String notes;
    private String location;
    
    // Medical screening information
    private Boolean hasMedicalClearance;
    private String screeningNotes;
}
