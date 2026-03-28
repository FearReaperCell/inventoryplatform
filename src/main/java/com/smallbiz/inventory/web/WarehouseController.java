package com.smallbiz.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @GetMapping
    public String warehouseHome() {
        return "redirect:/warehouse/floor";
    }

    @GetMapping("/floor")
    public String floor() {
        return "warehouse/floor";
    }

    @GetMapping("/receiving")
    public String receiving() {
        return "warehouse/receiving";
    }

    @GetMapping("/putaway")
    public String putaway() {
        return "warehouse/putaway";
    }

    @GetMapping("/cycle-count")
    public String cycleCount() {
        return "warehouse/cycle-count";
    }
}