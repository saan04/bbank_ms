package com.bloodbank.donor.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Data
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String bloodType;
    private String email;
    private String phoneNumber;
    private LocalDateTime lastDonationDate;
    private int donationCount;
    private boolean isEligible;
}
