package com.bloodbank.notification.dto;

import com.bloodbank.notification.model.NotificationType;
import lombok.Data;

@Data
public class NotificationRequest {
    private String recipient;
    private String subject;
    private String content;
    private NotificationType type;
    private String referenceId;
}
