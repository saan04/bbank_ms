package com.bloodbank.request.controller;

import com.bloodbank.request.model.BloodRequest;
import com.bloodbank.request.model.RequestStatus;
import com.bloodbank.request.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class BloodRequestController {
    
    @Autowired
    private BloodRequestService requestService;

    @PostMapping
    public ResponseEntity<BloodRequest> createRequest(@RequestBody BloodRequest request) {
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BloodRequest> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(requestService.updateRequestStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequest(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodRequest>> getRequestsByStatus(@PathVariable RequestStatus status) {
        return ResponseEntity.ok(requestService.getRequestsByStatus(status));
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<BloodRequest>> getRequestsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(requestService.getRequestsByBloodType(bloodType));
    }

    @GetMapping("/urgency/{level}")
    public ResponseEntity<List<BloodRequest>> getRequestsByUrgency(@PathVariable String level) {
        return ResponseEntity.ok(requestService.getRequestsByUrgency(level));
    }
}
