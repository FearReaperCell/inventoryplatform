package com.smallbiz.inventory.setup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.model.Role;
import com.smallbiz.inventory.model.Supplier;
import com.smallbiz.inventory.model.UserAccount;
import com.smallbiz.inventory.repo.InventoryItemRepository;
import com.smallbiz.inventory.repo.SupplierRepository;
import com.smallbiz.inventory.repo.UserAccountRepository;

@Component
public class DataSeeder implements CommandLineRunner {

  private final UserAccountRepository users;
  private final SupplierRepository suppliers;
  private final InventoryItemRepository items;
  private final PasswordEncoder encoder;

  public DataSeeder(UserAccountRepository users,
                    SupplierRepository suppliers,
                    InventoryItemRepository items,
                    PasswordEncoder encoder) {
    this.users = users;
    this.suppliers = suppliers;
    this.items = items;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {

    String adminPassword = encoder.encode("admin123");
    String employeePassword = encoder.encode("employee123");

    users.findByUsername("admin").orElseGet(() ->
        users.save(new UserAccount("admin", adminPassword, Role.ADMIN))
    );

    users.findByUsername("employee").orElseGet(() ->
        users.save(new UserAccount("employee", employeePassword, Role.EMPLOYEE))
    );

    var mainSupplier = suppliers.findByName("Main Supplier").orElseGet(() ->
        suppliers.save(new Supplier(
            "Main Supplier",
            "supplier@example.com",
            "555-0100",
            "Default supplier"
        ))
    );

    // Seed a couple of items if none exist
    if (items.count() == 0) {

      items.save(new InventoryItem(
          "SKU-1001",
          "Widget A",
          "Basic widget",
          8,
          10,
          25,
          "Aisle 1 - Bin 2",
          60,
          2.50,
          mainSupplier
      ));

      items.save(new InventoryItem(
          "SKU-2002",
          "Cable Pack",
          "10-pack cables",
          55,
          15,
          50,
          "Aisle 2 - Bin 1",
          80,
          9.99,
          mainSupplier
      ));
    }
  }
}