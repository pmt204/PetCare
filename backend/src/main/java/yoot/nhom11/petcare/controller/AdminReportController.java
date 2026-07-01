package yoot.nhom11.petcare.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.DoctorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for admin-level reporting.
 * All endpoints are secured under /api/admin/reports.
 */
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AdminReportController(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * API: GET /api/admin/reports/appointments/count?start=...&end=...
     */
    @GetMapping("/appointments/count")
    public ResponseEntity<Map<String, Long>> getAppointmentCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        long count = appointmentRepository.countByAppointmentTimeBetween(start, end);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * API: GET /api/admin/reports/workloads?date=yyyy-MM-dd
     * Function: Get doctor workloads. If date is not provided, returns all-time.
     */
    @GetMapping("/workloads")
    public ResponseEntity<List<Map<String, Object>>> getWorkloads(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Doctor> doctors = doctorRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Doctor doc : doctors) {
            long count;
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                count = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doc.getId(), startOfDay, endOfDay).size();
            } else {
                count = appointmentRepository.findByDoctorId(doc.getId()).size();
            }

            Map<String, Object> docMap = new HashMap<>();
            docMap.put("id", doc.getId());
            docMap.put("name", doc.getName());
            docMap.put("specialty", doc.getSpecialty() != null ? doc.getSpecialty() : "General Vet");
            docMap.put("appointmentsCount", count);
            docMap.put("workingHours", doc.getExperienceYears() != null ? "08:00 - 17:00" : "09:00 - 18:00");
            docMap.put("status", "ACTIVE");

            result.add(docMap);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * API: GET /api/admin/reports/appointments?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
     * Function: Get detailed list of appointments for Excel export. If dates are not provided, returns all-time.
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<Map<String, Object>>> getAppointmentsReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Appointment> appointments;
        if (startDate != null && endDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            appointments = appointmentRepository.findByAppointmentTimeBetween(start, end);
        } else {
            appointments = appointmentRepository.findAll();
        }

        List<Map<String, Object>> result = appointments.stream().map(app -> {
            String docName = app.getDoctor() != null ? app.getDoctor().getName() : 
                             (app.getVeterinarian() != null ? app.getVeterinarian().getFullName() : "Chưa chỉ định");
            String petName = app.getPet() != null ? app.getPet().getName() : 
                             (app.getPatientName() != null ? app.getPatientName() : "Chưa rõ");
            String ownerName = app.getOwner() != null ? app.getOwner().getFullName() : 
                               (app.getPatientName() != null ? app.getPatientName() : "Khách hàng");
            String timeStr = app.getAppointmentTime() != null ? app.getAppointmentTime().toString() : 
                             (app.getAppointmentAt() != null ? app.getAppointmentAt().toString() : "Chưa đặt lịch");
            
            Map<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("time", timeStr);
            map.put("doctorName", docName);
            map.put("petName", petName);
            map.put("ownerName", ownerName);
            map.put("reason", app.getReason() != null ? app.getReason() : (app.getReasonForVisit() != null ? app.getReasonForVisit() : "Khám bệnh"));
            map.put("status", app.getStatus() != null ? app.getStatus().name() : (app.getStatusStr() != null ? app.getStatusStr().toUpperCase() : "PENDING"));
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
