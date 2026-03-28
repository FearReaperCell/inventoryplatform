package com.smallbiz.inventory.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smallbiz.inventory.service.InventoryService;

@Controller
@RequestMapping("/items")
public class StockController {

  private final InventoryService inventory;

  public StockController(InventoryService inventory) {
    this.inventory = inventory;
  }

  @PostMapping("/{id}/stock-in")
  public String stockIn(@PathVariable Long id,
                        @RequestParam int amount,
                        @RequestParam(required = false) String note,
                        Authentication auth) {

    if (amount <= 0) {
      return "redirect:/?error=invalidAmount";
    }

    String user = (auth != null) ? auth.getName() : "system";

    inventory.stockIn(id, amount, user, note);

    return "redirect:/";
  }

  @PostMapping("/{id}/stock-out")
  public String stockOut(@PathVariable Long id,
                         @RequestParam int amount,
                         @RequestParam(required = false) String note,
                         Authentication auth) {

    if (amount <= 0) {
      return "redirect:/?error=invalidAmount";
    }

    String user = (auth != null) ? auth.getName() : "system";

    inventory.stockOut(id, amount, user, note);

    return "redirect:/";
  }
}