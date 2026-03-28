package com.smallbiz.inventory.web;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.repo.InventoryItemRepository;

@Controller
public class BarcodeController {

    private final InventoryItemRepository repo;

    public BarcodeController(InventoryItemRepository repo) {
        this.repo = repo;
    }

   @GetMapping("/barcode/{code}")
public String lookupBarcode(@PathVariable String code, Model model) {

    Optional<InventoryItem> item = repo.findByBarcode(code);

    if (item.isPresent()) {
        model.addAttribute("item", item.get());
        return "barcode-result";
    }

    return "redirect:/admin/items/new?barcode=" + code;
}

    @PostMapping("/barcode/stock-in/{code}")
    public String stockInByBarcode(@PathVariable String code) {

        Optional<InventoryItem> item = repo.findByBarcode(code);

        if(item.isPresent()){
            InventoryItem i = item.get();
            i.adjustQuantity(1);
            repo.save(i);
        }

        return "redirect:/scanner";
    }
}

