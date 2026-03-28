package com.smallbiz.inventory.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.smallbiz.inventory.model.ActivityLog;
import com.smallbiz.inventory.repo.ActivityLogRepository;

@Service
public class ActivityLogService {

    private final ActivityLogRepository repo;

    public ActivityLogService(ActivityLogRepository repo) {
        this.repo = repo;
    }

    public void log(String action, String username, String details) {
        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setUsername(username);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());

        repo.save(log);
    }

    public java.util.List<ActivityLog> recent() {
        return repo.findTop20ByOrderByTimestampDesc();
    }
}