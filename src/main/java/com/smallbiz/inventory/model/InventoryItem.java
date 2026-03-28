package com.smallbiz.inventory.model;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Entity
@Table(
    name = "inventory_items",
    indexes = {
        @Index(name = "idx_sku", columnList = "sku", unique = true)
    }
)
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String sku;

    @Column(unique = true)
    private String barcode;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @Min(0)
    @Column(nullable = false)
    private int quantityOnHand;

    @Min(0)
    @Column(nullable = false)
    private int reorderPoint;

    @Min(0)
    @Column(nullable = false)
    private int reorderQuantity;

    @NotBlank
    @Column(nullable = false)
    private String location;

    @Min(0)
    @Column(nullable = false)
    private int maxStockLevel;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private double unitCost;

    @Column
    private LocalDate expirationDate;

  @ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "supplier_id")
private Supplier preferredSupplier;

    @Column(nullable = false)
private Instant updatedAt;

@Version
private long version;

// SOFT DELETE FLAG
@Column(nullable = false)
private boolean active = true;

    public InventoryItem() {}

    public InventoryItem(
        String sku,
        String name,
        String description,
        int quantityOnHand,
        int reorderPoint,
        int reorderQuantity,
        String location,
        int maxStockLevel,
        double unitCost,
        Supplier preferredSupplier
    ) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderPoint = reorderPoint;
        this.reorderQuantity = reorderQuantity;
        this.location = location;
        this.maxStockLevel = maxStockLevel;
        this.unitCost = unitCost;
        this.preferredSupplier = preferredSupplier;
    }

    @PrePersist
    @PreUpdate
    public void touch() {
        this.updatedAt = Instant.now();
    }

    public boolean isLow() {
        return quantityOnHand <= reorderPoint;
    }

    public boolean isSurplus() {
        return quantityOnHand >= maxStockLevel;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public int getReorderQuantity() {
        return reorderQuantity;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxStockLevel() {
        return maxStockLevel;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Supplier getPreferredSupplier() {
        return preferredSupplier;
    }

    public Instant getUpdatedAt() {
    return updatedAt;
}

public boolean isActive() {
    return active;
}

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = Math.max(0, quantityOnHand);
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public void setReorderQuantity(int reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxStockLevel(int maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

   public void setPreferredSupplier(Supplier preferredSupplier) {
    this.preferredSupplier = preferredSupplier;
}

public void setActive(boolean active) {
    this.active = active;
}

    public void adjustQuantity(int delta) {
        this.quantityOnHand = Math.max(0, this.quantityOnHand + delta);
    }
}