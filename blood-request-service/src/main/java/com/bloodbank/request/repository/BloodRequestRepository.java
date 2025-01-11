package com.bloodbank.request.repository;

import com.bloodbank.request.model.BloodRequest;
import com.bloodbank.request.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByStatus(RequestStatus status);
    List<BloodRequest> findByBloodType(String bloodType);
    List<BloodRequest> findByUrgencyLevel(String urgencyLevel);
}
