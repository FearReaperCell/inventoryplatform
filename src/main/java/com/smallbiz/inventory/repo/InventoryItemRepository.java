package com.smallbiz.inventory.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smallbiz.inventory.model.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);

    Optional<InventoryItem> findByBarcode(String barcode);

    List<InventoryItem> findByActiveTrue();

}
