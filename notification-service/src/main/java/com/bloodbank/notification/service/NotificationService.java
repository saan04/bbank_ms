package com.bloodbank.notification.service;

import com.bloodbank.notification.dto.NotificationRequest;
import com.bloodbank.notification.model.Notification;
import com.bloodbank.notification.model.NotificationStatus;
import com.bloodbank.notification.model.NotificationType;
import com.bloodbank.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    
    private static final int MAX_RETRY_COUNT = 3;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private EmailService emailService;

    public Notification createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setRecipient(request.getRecipient());
        notification.setSubject(request.getSubject());
        notification.setContent(request.getContent());
        notification.setType(request.getType());
        notification.setReferenceId(request.getReferenceId());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRetryCount(0);
        
        return notificationRepository.save(notification);
    }

    public void sendNotification(Notification notification) {
        try {
            emailService.sendEmail(
                notification.getRecipient(),
                notification.getSubject(),
                notification.getContent()
            );
            
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            handleNotificationFailure(notification, e);
        }
        
        notificationRepository.save(notification);
    }

    private void handleNotificationFailure(Notification notification, Exception e) {
        notification.setRetryCount(notification.getRetryCount() + 1);
        
        if (notification.getRetryCount() >= MAX_RETRY_COUNT) {
            notification.setStatus(NotificationStatus.FAILED);
        } else {
            notification.setStatus(NotificationStatus.RETRYING);
        }
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository
            .findByStatusAndRetryCountLessThan(NotificationStatus.RETRYING, MAX_RETRY_COUNT);
        
        for (Notification notification : failedNotifications) {
            sendNotification(notification);
        }
    }

    public List<Notification> getNotificationsByStatus(NotificationStatus status) {
        return notificationRepository.findByStatus(status);
    }

    public List<Notification> getNotificationsByRecipient(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

    public List<Notification> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Template methods for common notifications
    public Notification sendDonationConfirmation(String recipient, String donorName, String bloodType) {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient(recipient);
        request.setSubject("Blood Donation Confirmation");
        request.setContent(String.format(
            "Dear %s,\n\nThank you for your blood donation of type %s. " +
            "Your contribution will help save lives!\n\nBest regards,\nBlood Bank Team",
            donorName, bloodType
        ));
        request.setType(NotificationType.DONATION_CONFIRMATION);
        
        Notification notification = createNotification(request);
        sendNotification(notification);
        return notification;
    }

    public Notification sendInventoryAlert(String recipient, String bloodType, Double currentLevel) {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient(recipient);
        request.setSubject("Blood Inventory Alert");
        request.setContent(String.format(
            "ALERT: Blood type %s inventory is low (Current level: %.2f units). " +
            "Immediate attention required.\n\nBest regards,\nBlood Bank Team",
            bloodType, currentLevel
        ));
        request.setType(NotificationType.INVENTORY_ALERT);
        
        Notification notification = createNotification(request);
        sendNotification(notification);
        return notification;
    }

    public Notification sendDonationReminder(String recipient, String donorName, LocalDateTime lastDonation) {
        NotificationRequest request = new NotificationRequest();
        request.setRecipient(recipient);
        request.setSubject("Blood Donation Reminder");
        request.setContent(String.format(
            "Dear %s,\n\nIt's been three months since your last donation on %s. " +
            "You are now eligible to donate blood again. " +
            "Please consider scheduling your next donation.\n\nBest regards,\nBlood Bank Team",
            donorName, lastDonation.toString()
        ));
        request.setType(NotificationType.DONATION_REMINDER);
        
        Notification notification = createNotification(request);
        sendNotification(notification);
        return notification;
    }
}
