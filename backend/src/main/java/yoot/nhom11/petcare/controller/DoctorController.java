package yoot.nhom11.petcare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.DoctorResponse;
import yoot.nhom11.petcare.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    /**
     * Create a new doctor.
     * API: POST /api/doctors
     * Function: add a doctor to the system
     */
    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest request) {
        DoctorResponse r = service.create(request);
        return ResponseEntity.ok(r);
    }

    /**
     * Get doctor by id.
     * API: GET /api/doctors/{id}
     * Function: retrieve a single doctor's details
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * List all doctors.
     * API: GET /api/doctors
     * Function: list available doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    /**
     * Update a doctor.
     * API: PUT /api/doctors/{id}
     * Function: update doctor's information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(@PathVariable Long id, @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    /**
     * Delete a doctor.
     * API: DELETE /api/doctors/{id}
     * Function: remove a doctor from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
