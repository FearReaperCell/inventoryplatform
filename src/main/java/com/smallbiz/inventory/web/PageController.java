package com.smallbiz.inventory.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.repo.InventoryItemRepository;

@Controller
public class PageController {

    private final InventoryItemRepository itemRepo;

    public PageController(InventoryItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Categories");
        return "inventory/categories";
    }

    @GetMapping("/locations")
    public String locations(Model model) {
        model.addAttribute("pageTitle", "Locations / Bins");
        return "inventory/locations";
    }

    @GetMapping("/low-stock")
    public String lowStock(Model model) {

        List<InventoryItem> lowItems = itemRepo.findAll()
                .stream()
                .filter(InventoryItem::isLow)
                .toList();

        model.addAttribute("pageTitle", "Low Stock Items");
        model.addAttribute("items", lowItems);

        return "inventory/low-stock";
    }

    @GetMapping("/stock/in")
    public String stockIn(Model model) {
        model.addAttribute("pageTitle", "Stock In");
        return "inventory/stock-in";
    }

    @GetMapping("/stock/out")
    public String stockOut(Model model) {
        model.addAttribute("pageTitle", "Stock Out");
        return "inventory/stock-out";
    }

    @GetMapping("/transfers")
    public String transfers(Model model) {
        model.addAttribute("pageTitle", "Transfers");
        return "inventory/transfers";
    }

    @GetMapping("/adjustments")
    public String adjustments(Model model) {
        model.addAttribute("pageTitle", "Adjustments");
        return "inventory/adjustments";
    }

    @GetMapping("/items/search")
    public String searchItems(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String q,
            Model model) {

        if (q == null || q.isBlank()) {
            model.addAttribute("items", List.of());
            return "inventory/search-results";
        }

        List<InventoryItem> results;

        switch (type) {

            case "sku":
                results = itemRepo.findAll()
                        .stream()
                        .filter(i -> i.getSku() != null &&
                                i.getSku().toLowerCase().contains(q.toLowerCase()))
                        .toList();
                break;

            case "name":
                results = itemRepo.findAll()
                        .stream()
                        .filter(i -> i.getName() != null &&
                                i.getName().toLowerCase().contains(q.toLowerCase()))
                        .toList();
                break;

            case "nsn":
                results = itemRepo.findAll()
                        .stream()
                        .filter(i -> i.getBarcode() != null &&
                                i.getBarcode().toLowerCase().contains(q.toLowerCase()))
                        .toList();
                break;

            default:
                results = List.of();
        }

        model.addAttribute("items", results);
        model.addAttribute("query", q);

        return "inventory/search-results";
    }
    @GetMapping("/scanner")
public String scanner() {
    return "scanner";
}
}