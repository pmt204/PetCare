package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.request.PetServiceRequest;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;
import yoot.nhom11.petcare.service.PetServiceService;

import java.util.List;

/**
 * Controller for admin-level pet service management.
 * All endpoints are secured under /api/admin/services.
 */
@RestController
@RequestMapping("/api/admin/services")
public class AdminPetServiceController {

    private final PetServiceService petServiceService;

    public AdminPetServiceController(PetServiceService petServiceService) {
        this.petServiceService = petServiceService;
    }

    /**
     * API: POST /api/admin/services
     * Function: Create a new pet service.
     * @param request The request body containing service details.
     * @return The created service.
     */
    @PostMapping
    public ResponseEntity<PetServiceResponse> create(@RequestBody PetServiceRequest request) {
        return ResponseEntity.ok(petServiceService.create(request));
    }

    /**
     * API: GET /api/admin/services/{id}
     * Function: Get a pet service by its ID.
     * @param id The ID of the service.
     * @return The service details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetServiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(petServiceService.getById(id));
    }

    /**
     * API: GET /api/admin/services
     * Function: Get a list of all pet services.
     * @return A list of all services.
     */
    @GetMapping
    public ResponseEntity<List<PetServiceResponse>> listAll() {
        return ResponseEntity.ok(petServiceService.listAll());
    }

    /**
     * API: PUT /api/admin/services/{id}
     * Function: Update an existing pet service.
     * @param id The ID of the service to update.
     * @param request The request body with updated details.
     * @return The updated service.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PetServiceResponse> update(@PathVariable Long id, @RequestBody PetServiceRequest request) {
        return ResponseEntity.ok(petServiceService.update(id, request));
    }

    /**
     * API: DELETE /api/admin/services/{id}
     * Function: Delete a pet service.
     * @param id The ID of the service to delete.
     * @return No content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
