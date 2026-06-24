package yoot.nhom11.petcare.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yoot.nhom11.petcare.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller for admin-level reporting.
 * All endpoints are secured under /api/admin/reports.
 */
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final AppointmentRepository appointmentRepository;

    public AdminReportController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * API: GET /api/admin/reports/appointments/count?start=...&end=...
     * Function: Get the number of appointments within a given date-time range.
     * @param start The start of the date-time range (ISO 8601 format).
     * @param end The end of the date-time range (ISO 8601 format).
     * @return A map containing the appointment count.
     */
    @GetMapping("/appointments/count")
    public ResponseEntity<Map<String, Long>> getAppointmentCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        long count = appointmentRepository.countByAppointmentTimeBetween(start, end);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
