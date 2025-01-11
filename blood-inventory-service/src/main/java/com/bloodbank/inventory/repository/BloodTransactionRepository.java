package com.bloodbank.inventory.repository;

import com.bloodbank.inventory.model.BloodTransaction;
import com.bloodbank.inventory.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodTransactionRepository extends JpaRepository<BloodTransaction, Long> {
    List<BloodTransaction> findByBloodType(String bloodType);
    List<BloodTransaction> findByType(TransactionType type);
    List<BloodTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<BloodTransaction> findByReferenceId(String referenceId);
    List<BloodTransaction> findByLocation(String location);
}
