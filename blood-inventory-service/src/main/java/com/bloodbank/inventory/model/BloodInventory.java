package com.bloodbank.inventory.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class BloodInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String bloodType;
    
    private Double availableUnits;
    private Double reservedUnits;
    
    private Double minimumThreshold;
    private Double maximumThreshold;
    
    private LocalDateTime lastUpdated;
    private String location;
    
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
}
