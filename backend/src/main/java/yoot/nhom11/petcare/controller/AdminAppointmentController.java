package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.service.impl.AppointmentServiceImpl;

import java.util.List;

/**
 * Controller for admin-level appointment management.
 * All endpoints are secured under /api/admin/appointments.
 */
@RestController
@RequestMapping("/api/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentServiceImpl appointmentService;

    public AdminAppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * API: GET /api/admin/appointments
     * Function: Get a list of all appointments.
     * @return A list of all appointments.
     */
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> listAll() {
        return ResponseEntity.ok(appointmentService.listAll());
    }

    /**
     * API: PUT /api/admin/appointments/{id}/cancel
     * Function: Cancel an appointment by changing its status to CANCELLED.
     * @param id The ID of the appointment to cancel.
     * @return The cancelled appointment response.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }
}
