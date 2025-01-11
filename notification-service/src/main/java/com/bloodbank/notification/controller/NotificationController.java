package com.bloodbank.notification.controller;

import com.bloodbank.notification.dto.NotificationRequest;
import com.bloodbank.notification.model.Notification;
import com.bloodbank.notification.model.NotificationStatus;
import com.bloodbank.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest request) {
        Notification notification = notificationService.createNotification(request);
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/donation-confirmation")
    public ResponseEntity<Notification> sendDonationConfirmation(
            @RequestParam String recipient,
            @RequestParam String donorName,
            @RequestParam String bloodType) {
        return ResponseEntity.ok(notificationService.sendDonationConfirmation(recipient, donorName, bloodType));
    }

    @PostMapping("/inventory-alert")
    public ResponseEntity<Notification> sendInventoryAlert(
            @RequestParam String recipient,
            @RequestParam String bloodType,
            @RequestParam Double currentLevel) {
        return ResponseEntity.ok(notificationService.sendInventoryAlert(recipient, bloodType, currentLevel));
    }

    @PostMapping("/donation-reminder")
    public ResponseEntity<Notification> sendDonationReminder(
            @RequestParam String recipient,
            @RequestParam String donorName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastDonation) {
        return ResponseEntity.ok(notificationService.sendDonationReminder(recipient, donorName, lastDonation));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Notification>> getNotificationsByStatus(@PathVariable NotificationStatus status) {
        return ResponseEntity.ok(notificationService.getNotificationsByStatus(status));
    }

    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<List<Notification>> getNotificationsByRecipient(@PathVariable String recipient) {
        return ResponseEntity.ok(notificationService.getNotificationsByRecipient(recipient));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Notification>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(notificationService.getNotificationsByDateRange(startDate, endDate));
    }
}
