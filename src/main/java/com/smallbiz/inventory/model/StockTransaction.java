package com.smallbiz.inventory.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "stock_txns")
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private StockTxnType type;

    @Column(nullable = false)
    private int delta; // +in, -out

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String performedBy;

    @Column
    private String note;

    protected StockTransaction() {}

    public StockTransaction(InventoryItem item, StockTxnType type, int delta, String performedBy, String note) {
        this.item = item;
        this.type = type;
        this.delta = delta;
        this.performedBy = performedBy;
        this.note = note;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public InventoryItem getItem() {
        return item;
    }

    public StockTxnType getType() {
        return type;
    }

    public int getDelta() {
        return delta;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getNote() {
        return note;
    }
}