package com.smallbiz.inventory.service;

import org.springframework.stereotype.Component;

import com.smallbiz.inventory.repo.InventoryItemRepository;
import com.smallbiz.inventory.repo.ItemAlertStateRepository;

@Component
public class AlertScheduler {

  private final InventoryItemRepository items;
  private final ItemAlertStateRepository states;

  public AlertScheduler(InventoryItemRepository items,
                        ItemAlertStateRepository states) {
    this.items = items;
    this.states = states;
  }

}