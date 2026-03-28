package com.smallbiz.inventory.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smallbiz.inventory.model.InventoryItem;
import com.smallbiz.inventory.service.ActivityLogService;
import com.smallbiz.inventory.service.InventoryService;
import com.smallbiz.inventory.service.ReorderSuggestion;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportsController {

  private final InventoryService inventory;
  private final ActivityLogService activityLogService;

  // ✅ UPDATED CONSTRUCTOR
  public AdminReportsController(InventoryService inventory, ActivityLogService activityLogService) {
    this.inventory = inventory;
    this.activityLogService = activityLogService;
  }

  @GetMapping("/reorder")
  public String reorderPage(Model model) {

    var suggestions = inventory.reorderSuggestions();

    double totalCost = suggestions.stream()
        .mapToDouble(s -> s.getReorderQty() * s.getUnitCost())
        .sum();

    model.addAttribute("suggestions", suggestions);
    model.addAttribute("totalCost", totalCost);

    return "admin/reports-reorder";
  }

  @PostMapping("/reorder.pdf")
  public String processReorder(
          @RequestParam String sku,
          @RequestParam int amount) {

      InventoryItem item = inventory.listAll().stream()
              .filter(i -> i.getSku().equals(sku))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Item not found"));

      inventory.stockIn(
          item.getId(),
          amount,
          "system",
          "Auto reorder"
      );

      // 🔔 LOG ACTIVITY
      activityLogService.log("REORDER", "system",
          "Auto reordered " + amount + " of " + item.getName());

      return "redirect:/admin/reports/reorder";
  }

  // ✅ FIXED ACTIVITY PAGE (THIS WAS YOUR ISSUE)
  @GetMapping("/activity")
  public String activityPage(Model model) {

    model.addAttribute("activities", activityLogService.recent());

    return "admin/reports-activity";
  }

  @GetMapping("/expiring")
  public String expiring(Model model) {

    LocalDate today = LocalDate.now();
    LocalDate limit = today.plusDays(14);

    var expiringItems = inventory.listAll().stream()
            .filter(item -> item.getExpirationDate() != null)
            .filter(item -> !item.getExpirationDate().isBefore(today))
            .filter(item -> !item.getExpirationDate().isAfter(limit))
            .toList();

    model.addAttribute("items", expiringItems);

    return "admin/reports-expiring";
  }

  @GetMapping("/reorder/pdf")
  public void reorderPdf(HttpServletResponse response) throws Exception {

    response.setContentType("application/pdf");
    response.setHeader(
        "Content-Disposition",
        "attachment; filename=reorder_report_" + LocalDate.now() + ".pdf"
    );

    var suggestions = inventory.reorderSuggestions();

    double totalCost = suggestions.stream()
        .mapToDouble(s -> s.getUnitCost() * s.getReorderQty())
        .sum();

    com.itextpdf.kernel.pdf.PdfWriter writer =
            new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());

    com.itextpdf.kernel.pdf.PdfDocument pdf =
            new com.itextpdf.kernel.pdf.PdfDocument(writer);

    com.itextpdf.layout.Document document =
            new com.itextpdf.layout.Document(pdf);

    document.add(new com.itextpdf.layout.element.Paragraph("Reorder Report"));

    com.itextpdf.layout.element.Table table =
            new com.itextpdf.layout.element.Table(9);

    table.addCell("SKU");
    table.addCell("Name");
    table.addCell("On Hand");
    table.addCell("Reorder Point");
    table.addCell("Reorder Qty");
    table.addCell("Unit Cost");
    table.addCell("Total Cost");
    table.addCell("Supplier");
    table.addCell("Location");

    for (ReorderSuggestion s : suggestions) {

        table.addCell(s.getSku());
        table.addCell(s.getName());
        table.addCell(String.valueOf(s.getQtyOnHand()));
        table.addCell(String.valueOf(s.getReorderPoint()));
        table.addCell(String.valueOf(s.getReorderQty()));
        table.addCell(String.valueOf(s.getUnitCost()));
        table.addCell(String.valueOf(s.getUnitCost() * s.getReorderQty()));
        table.addCell(s.getSupplierName());
        table.addCell(s.getLocation());
    }

    document.add(table);

    document.add(new com.itextpdf.layout.element.Paragraph(
        "Total Reorder Cost: $" + String.format("%.2f", totalCost)
    ).setBold());

    document.close();
  }
}