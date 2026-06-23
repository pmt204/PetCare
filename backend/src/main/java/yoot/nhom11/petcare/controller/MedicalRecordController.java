package yoot.nhom11.petcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.service.MedicalRecordService;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDetailResponse> getMedicalRecordDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordDetail(id));
    }
}
