package com.bloodbank.inventory.repository;

import com.bloodbank.inventory.model.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    Optional<BloodInventory> findByBloodGroup(String bloodGroup);
    boolean existsByBloodGroup(String bloodGroup);
}
