package com.bloodbank.notification.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String recipient;
    private String subject;
    
    @Column(length = 1000)
    private String content;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    
    private String referenceId; // ID of the related entity (donation, inventory, etc.)
    private Integer retryCount;
}
