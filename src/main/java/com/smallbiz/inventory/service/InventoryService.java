package com.smallbiz.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.model.StockTransaction;
import com.smallbiz.inventory.model.StockTxnType;
import com.smallbiz.inventory.repo.InventoryItemRepository;
import com.smallbiz.inventory.repo.StockTransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryService {

  private final InventoryItemRepository items;
  private final StockTransactionRepository txns;
  private final EmailService emailService;

  public InventoryService(
      InventoryItemRepository items,
      StockTransactionRepository txns,
      EmailService emailService
  ) {
    this.items = items;
    this.txns = txns;
    this.emailService = emailService;
  }

  public List<InventoryItem> listAll() {
    return items.findAll();
  }

  public InventoryItem getOrThrow(Long id) {
    return items.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Item not found"));
  }

  @Transactional
  public InventoryItem createItem(InventoryItem item) {

    items.findBySku(item.getSku()).ifPresent(x -> {
      throw new IllegalArgumentException("SKU already exists: " + item.getSku());
    });

    if (item.getMaxStockLevel() < item.getReorderPoint()) {
      throw new IllegalArgumentException("Max stock level must be >= reorder point.");
    }

    return items.save(item);
  }

  @Transactional
  public InventoryItem updateItem(Long id, InventoryItem updated) {

    InventoryItem existing = getOrThrow(id);

    if (!existing.getSku().equals(updated.getSku())) {
      items.findBySku(updated.getSku()).ifPresent(x -> {
        throw new IllegalArgumentException("SKU already exists: " + updated.getSku());
      });
    }

    if (updated.getMaxStockLevel() < updated.getReorderPoint()) {
      throw new IllegalArgumentException("Max stock level must be >= reorder point.");
    }

    existing.setSku(updated.getSku());
    existing.setBarcode(updated.getBarcode());
    existing.setName(updated.getName());
    existing.setDescription(updated.getDescription());
    existing.setReorderPoint(updated.getReorderPoint());
    existing.setReorderQuantity(updated.getReorderQuantity());
    existing.setLocation(updated.getLocation());
    existing.setMaxStockLevel(updated.getMaxStockLevel());
    existing.setUnitCost(updated.getUnitCost());
    existing.setExpirationDate(updated.getExpirationDate());
    existing.setPreferredSupplier(updated.getPreferredSupplier());
    existing.setQuantityOnHand(updated.getQuantityOnHand());

    InventoryItem saved = items.save(existing);

    // ✅ EXPIRATION ALERT
    if (saved.getExpirationDate() != null &&
        saved.getExpirationDate().isBefore(java.time.LocalDate.now().plusDays(14))) {

        emailService.sendAlert(
            "Expiration Alert",
            "Item " + saved.getName() +
            " is expiring soon on " + saved.getExpirationDate()
        );
    }

    return saved;
  }

  @Transactional
  public void deleteItem(Long id) {
    items.deleteById(id);
  }

  @Transactional
  public void stockIn(Long itemId, int amount, String byUser, String note) {

    if (amount <= 0) {
      throw new IllegalArgumentException("Stock-in amount must be > 0.");
    }

    InventoryItem item = getOrThrow(itemId);

    long newQty = (long) item.getQuantityOnHand() + amount;

    if (newQty > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Stock-in too large; verify entry.");
    }

    item.adjustQuantity(amount);
    items.save(item);

    txns.save(new StockTransaction(item, StockTxnType.STOCK_IN, amount, byUser, note));
  }

  @Transactional
  public void stockOut(Long itemId, int amount, String byUser, String note) {

    if (amount <= 0) {
      throw new IllegalArgumentException("Stock-out amount must be > 0.");
    }

    InventoryItem item = getOrThrow(itemId);

    int newQty = item.getQuantityOnHand() - amount;

    if (newQty < 0) {
      throw new IllegalArgumentException("Not enough stock. Current qty: " + item.getQuantityOnHand());
    }

    item.adjustQuantity(-amount);
    items.save(item);

    txns.save(new StockTransaction(item, StockTxnType.STOCK_OUT, -amount, byUser, note));

    // ✅ LOW STOCK ALERT
    if (item.getQuantityOnHand() <= item.getReorderPoint()) {

        emailService.sendAlert(
            "Low Stock Alert",
            "Item " + item.getName() +
            " is low on stock.\nCurrent Quantity: " + item.getQuantityOnHand() +
            "\nReorder Point: " + item.getReorderPoint()
        );
    }
  }

  public List<StockTransaction> recentTxns() {
    return txns.findTop50ByOrderByCreatedAtDesc();
  }

  // 🔥 FIXED METHOD (THIS IS THE IMPORTANT PART)
  public List<ReorderSuggestion> reorderSuggestions() {

    return items.findAll().stream()
        .filter(i -> i.getQuantityOnHand() <= i.getReorderPoint())
        .map(i -> {

            int reorderAmount = Math.max(0,
                i.getMaxStockLevel() - i.getQuantityOnHand()
            );

            // ✅ FIXED SUPPLIER NAME LOGIC
            String supplierName = "Unknown Supplier";

            if (i.getPreferredSupplier() != null &&
                i.getPreferredSupplier().getName() != null) {

                supplierName = i.getPreferredSupplier().getName();
            }

            return new ReorderSuggestion(
                i.getSku(),
                i.getName(),
                i.getQuantityOnHand(),
                i.getReorderPoint(),
                reorderAmount,
                supplierName, // ✅ FIXED HERE
                i.getLocation(),
                i.getUnitCost()
            );
        })
        .toList();
  }

  public InventoryItem findBySku(String sku) {
    return items.findBySku(sku)
        .orElseThrow(() -> new IllegalArgumentException("Item not found: " + sku));
  }

  public List<String> randomSuppliers() {

    List<String> suppliers = List.of(
        "Global Tech Supply",
        "Prime Industrial Co.",
        "Apex Components",
        "NorthStar Logistics",
        "BlueLine Distribution",
        "Titan Wholesale",
        "Summit Parts Group",
        "Velocity Supply Chain",
        "Pioneer Equipment",
        "Atlas Inventory Systems"
    );

    java.util.Collections.shuffle(suppliers);

    return suppliers;
  }
}