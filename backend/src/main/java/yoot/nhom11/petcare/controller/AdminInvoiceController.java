package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.request.InvoiceRequest;
import yoot.nhom11.petcare.dto.response.InvoiceResponse;
import yoot.nhom11.petcare.service.InvoiceService;

import java.util.List;

/**
 * Controller for admin-level invoice management.
 * All endpoints are secured under /api/admin/invoices.
 */
@RestController
@RequestMapping("/api/admin/invoices")
public class AdminInvoiceController {

    private final InvoiceService invoiceService;

    public AdminInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * API: POST /api/admin/invoices
     * Function: Create a new invoice for an appointment.
     * @param request The request body containing invoice details.
     * @return The created invoice.
     */
    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.create(request));
    }

    /**
     * API: GET /api/admin/invoices/{id}
     * Function: Get an invoice by its ID.
     * @param id The ID of the invoice.
     * @return The invoice details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    /**
     * API: GET /api/admin/invoices
     * Function: Get a list of all invoices.
     * @return A list of all invoices.
     */
    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> listAll() {
        return ResponseEntity.ok(invoiceService.listAll());
    }

    /**
     * API: PUT /api/admin/invoices/{id}/payment-status
     * Function: Update the payment status of an invoice.
     * @param id The ID of the invoice to update.
     * @param request The request body with the new payment status.
     * @return The updated invoice.
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<InvoiceResponse> updatePaymentStatus(@PathVariable Long id, @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.updatePaymentStatus(id, request));
    }
}
