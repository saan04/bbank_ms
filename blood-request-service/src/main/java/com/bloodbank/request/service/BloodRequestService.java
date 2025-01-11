package com.bloodbank.request.service;

import com.bloodbank.request.model.BloodRequest;
import com.bloodbank.request.model.RequestStatus;
import com.bloodbank.request.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloodRequestService {
    
    @Autowired
    private BloodRequestRepository requestRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    public BloodRequest createRequest(BloodRequest request) {
        // Check blood availability
        Boolean isAvailable = restTemplate.getForObject(
            "http://localhost:8083/api/inventory/check-availability?bloodType=" + request.getBloodType() + "&quantity=" + request.getUnitsRequired(),
            Boolean.class
        );

        request.setRequestDate(LocalDateTime.now());
        request.setStatus(isAvailable ? RequestStatus.APPROVED : RequestStatus.PENDING);

        BloodRequest savedRequest = requestRepository.save(request);

        // Send notification
        if (savedRequest.getStatus() == RequestStatus.APPROVED) {
            restTemplate.postForObject(
                "http://localhost:8084/api/notifications",
                createNotificationRequest(savedRequest, "APPROVED"),
                Object.class
            );
        } else {
            restTemplate.postForObject(
                "http://localhost:8084/api/notifications",
                createNotificationRequest(savedRequest, "PENDING"),
                Object.class
            );
        }

        return savedRequest;
    }

    public BloodRequest updateRequestStatus(Long requestId, RequestStatus newStatus) {
        BloodRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(newStatus);

        if (newStatus == RequestStatus.FULFILLED) {
            // Update inventory
            restTemplate.postForObject(
                "http://localhost:8083/api/inventory/request",
                String.format("bloodType=%s&quantity=%s&location=%s&requestId=%s",
                    request.getBloodType(),
                    request.getUnitsRequired(),
                    request.getHospitalName(),
                    request.getId()
                ),
                Object.class
            );
        }

        BloodRequest updatedRequest = requestRepository.save(request);

        // Send notification
        restTemplate.postForObject(
            "http://localhost:8084/api/notifications",
            createNotificationRequest(updatedRequest, newStatus.toString()),
            Object.class
        );

        return updatedRequest;
    }

    private Object createNotificationRequest(BloodRequest request, String status) {
        return new Object() {
            public final String type = "BLOOD_REQUEST";
            public final String recipient = request.getContactEmail();
            public final String subject = "Blood Request " + status;
            public final String content = String.format(
                "Blood request for %s units of %s blood is %s. Hospital: %s",
                request.getUnitsRequired(),
                request.getBloodType(),
                status,
                request.getHospitalName()
            );
            public final String referenceId = request.getId().toString();
        };
    }

    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public BloodRequest getRequest(Long id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public List<BloodRequest> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    public List<BloodRequest> getRequestsByBloodType(String bloodType) {
        return requestRepository.findByBloodType(bloodType);
    }

    public List<BloodRequest> getRequestsByUrgency(String urgencyLevel) {
        return requestRepository.findByUrgencyLevel(urgencyLevel);
    }
}
