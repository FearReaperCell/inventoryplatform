package com.smallbiz.inventory.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_alert_state")
public class ItemAlertState {

    @Id
    @Column(name = "item_id")
    private Long itemId;

    @Column(nullable = false)
    private boolean lowAlertSent = false;

    @Column(nullable = false)
    private Instant updatedAt;

    protected ItemAlertState() {}

    public ItemAlertState(Long itemId) {
        this.itemId = itemId;
        this.lowAlertSent = false;
    }

    @PrePersist
    @PreUpdate
    public void touch() {
        this.updatedAt = Instant.now();
    }

    public Long getItemId() {
        return itemId;
    }

    public boolean isLowAlertSent() {
        return lowAlertSent;
    }

    public void setLowAlertSent(boolean lowAlertSent) {
        this.lowAlertSent = lowAlertSent;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
