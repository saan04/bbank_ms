package com.bloodbank.inventory.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class BloodTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bloodType;
    private Double quantity;
    private LocalDateTime transactionDate;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    private String referenceId; // donation ID or request ID
    private String notes;
    private String location;
}
