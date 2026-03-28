package com.smallbiz.inventory.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smallbiz.inventory.repo.InventoryItemRepository;

@Controller
public class DashboardController {

    private final InventoryItemRepository itemRepo;

    public DashboardController(InventoryItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping("/")
    public String dashboard(Model model){

        var items = itemRepo.findAll();

        double totalValue = items.stream()
                .mapToDouble(i -> i.getQuantityOnHand() * i.getUnitCost())
                .sum();

        var lowStockItems = items.stream()
                .filter(i -> i.getQuantityOnHand() <= 5)
                .toList();

        var surplusItems = items.stream()
                .filter(i -> i.getQuantityOnHand() >= 50)
                .toList();

        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(14);

        var expiringItems = items.stream()
                .filter(i -> i.getExpirationDate() != null)
                .filter(i -> !i.getExpirationDate().isBefore(today))
                .filter(i -> !i.getExpirationDate().isAfter(limit))
                .toList();

        // ✅ ADD POPUP ALERT HERE
        if (!lowStockItems.isEmpty()) {
            model.addAttribute("alertMessage", 
                "⚠️ You have " + lowStockItems.size() + " low stock items!");
        }

        model.addAttribute("totalValue", totalValue);
        model.addAttribute("totalItems", items.size());
        model.addAttribute("lowStockCount", lowStockItems.size());
        model.addAttribute("surplusStockCount", surplusItems.size());
        model.addAttribute("expiringCount", expiringItems.size());
        model.addAttribute("lowStock", lowStockItems);
        model.addAttribute("surplusStock", surplusItems);

        return "dashboard";
    }
}