package com.smallbiz.inventory.service;

public class ReorderSuggestion {

    private String sku;
    private String name;
    private int qtyOnHand;
    private int reorderPoint;
    private int reorderQty;
    private String supplierName;
    private String location;
    private double unitCost; // ✅ NEW

    public ReorderSuggestion(
            String sku,
            String name,
            int qtyOnHand,
            int reorderPoint,
            int reorderQty,
            String supplierName,
            String location,
            double unitCost // new 
    ) {
        this.sku = sku;
        this.name = name;
        this.qtyOnHand = qtyOnHand;
        this.reorderPoint = reorderPoint;
        this.reorderQty = reorderQty;
        this.supplierName = supplierName;
        this.location = location;
        this.unitCost = unitCost;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public int getReorderQty() {
        return reorderQty;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getLocation() {
        return location;
    }

    public double getUnitCost() {
        return unitCost;
    }
}
