package com.bloodbank.donation.dto;

import lombok.Data;

@Data
public class DonationRequest {
    private Long donorId;
    private String bloodType;
    private Double quantity;
    private String location;
    private String notes;
}
