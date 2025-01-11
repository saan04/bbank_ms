package com.bloodbank.notification.repository;

import com.bloodbank.notification.model.Notification;
import com.bloodbank.notification.model.NotificationStatus;
import com.bloodbank.notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
    List<Notification> findByType(NotificationType type);
    List<Notification> findByRecipient(String recipient);
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Notification> findByStatusAndRetryCountLessThan(NotificationStatus status, Integer maxRetries);
}
