package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;
import yoot.nhom11.petcare.service.MedicalRecordService;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponse> create(@RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}