package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;
import yoot.nhom11.petcare.service.PetServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class PetServiceController {

    private final PetServiceService petServiceService;

    public PetServiceController(PetServiceService petServiceService) {
        this.petServiceService = petServiceService;
    }

    @GetMapping
    public ResponseEntity<List<PetServiceResponse>> listAll() {
        return ResponseEntity.ok(petServiceService.listAll());
    }
}
