package com.bloodbank.request.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String patientName;
    private String bloodType;
    private Double unitsRequired;
    private String hospitalName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String urgencyLevel; // NORMAL, URGENT, EMERGENCY
    private LocalDateTime requestDate;
    private LocalDateTime requiredBy;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    private String notes;
}
