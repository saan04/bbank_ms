package com.bloodbank.inventory.repository;

import com.bloodbank.inventory.model.BloodInventory;
import com.bloodbank.inventory.model.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    Optional<BloodInventory> findByBloodType(String bloodType);
    List<BloodInventory> findByStatus(InventoryStatus status);
    List<BloodInventory> findByLocation(String location);
    Optional<BloodInventory> findByBloodTypeAndLocation(String bloodType, String location);
}
