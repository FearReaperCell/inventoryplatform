package com.smallbiz.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.repo.InventoryItemRepository;


@RequestMapping("/admin/items")
@Controller
public class AdminItemsController {

    private final InventoryItemRepository itemRepo;

    public AdminItemsController(InventoryItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    // Show items list
    @GetMapping
    public String listItems(Model model) {

        model.addAttribute("items", itemRepo.findByActiveTrue());

        return "items";
    }

    // Open new item page
   @GetMapping("/new")
public String newItem(@RequestParam(required = false) String barcode, Model model) {

    InventoryItem item = new InventoryItem();

    if(barcode != null){
        item.setBarcode(barcode);
    }

    model.addAttribute("item", item);

    return "items-new";
}

    

    @PostMapping("/save")
public String saveItem(@ModelAttribute InventoryItem item, Model model) {

    try {
        if (item.getId() != null) {
            InventoryItem existing = itemRepo.findById(item.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

            existing.setSku(item.getSku());
            existing.setName(item.getName());
            existing.setBarcode(item.getBarcode());
            existing.setQuantityOnHand(item.getQuantityOnHand());
            existing.setLocation(item.getLocation());
            existing.setExpirationDate(item.getExpirationDate());

            itemRepo.save(existing);
        } else {
            itemRepo.save(item);
        }

        return "redirect:/admin/items";

    } catch (Exception e) {
        model.addAttribute("message", "Unable to save item: " + e.getMessage());
        return "item-error";
    }
}

    // Open edit page
   @GetMapping("/{id}/edit")
public String editItem(@PathVariable Long id, Model model) {

    InventoryItem item = itemRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

    model.addAttribute("item", item);

    return "items-edit";
}

@PostMapping("/update")
public String updateItem(@ModelAttribute InventoryItem formItem) {

    InventoryItem item = itemRepo.findById(formItem.getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

    item.setSku(formItem.getSku());
    item.setName(formItem.getName());
    item.setQuantityOnHand(formItem.getQuantityOnHand());
    item.setLocation(formItem.getLocation());

    // ✅ FIXED
    item.setExpirationDate(formItem.getExpirationDate());

    // Fix barcode issue
    if(formItem.getBarcode() == null || formItem.getBarcode().isBlank()) {
        item.setBarcode(null);
    } else {
        item.setBarcode(formItem.getBarcode());
    }

    itemRepo.save(item);

    return "redirect:/admin/items";
}
@GetMapping("/{id}/delete")
public String deleteItem(@PathVariable Long id) {

    InventoryItem item = itemRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

    item.setActive(false);

    itemRepo.save(item);

    return "redirect:/admin/items";
}


}
