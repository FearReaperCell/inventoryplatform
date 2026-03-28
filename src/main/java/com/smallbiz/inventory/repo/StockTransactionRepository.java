package com.smallbiz.inventory.repo;

import com.smallbiz.inventory.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
  List<StockTransaction> findTop50ByOrderByCreatedAtDesc();
}
