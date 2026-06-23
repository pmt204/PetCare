package yoot.nhom11.petcare.controller;

import java.util.List;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.dto.response.DoctorResponse;
import yoot.nhom11.petcare.service.AppointmentService;
import yoot.nhom11.petcare.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public DoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    /**
     * Create a new doctor.
     * API: POST /api/doctors
     * Function: add a doctor to the system
     */
    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest request) {
        DoctorResponse r = doctorService.create(request);
        return ResponseEntity.ok(r);
    }

    /**
     * Get doctor by id.
     * API: GET /api/doctors/{id}
     * Function: retrieve a single doctor's details
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    /**
     * List all doctors.
     * API: GET /api/doctors
     * Function: list available doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> listAll() {
        return ResponseEntity.ok(doctorService.listAll());
    }

    /**
     * Update a doctor.
     * API: PUT /api/doctors/{id}
     * Function: update doctor's information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(@PathVariable Long id, @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    /**
     * Delete a doctor.
     * API: DELETE /api/doctors/{id}
     * Function: remove a doctor from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get doctor's appointments for a specific day.
     * API: GET /api/doctors/{id}/appointments?date=yyyy-MM-dd
     * Function: retrieve a list of appointments for a doctor on a given day
     */
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsForDay(
            @PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByDoctorAndDate(id, date));
    }
}
