package com.smallbiz.inventory.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smallbiz.inventory.model.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop20ByOrderByTimestampDesc();
}