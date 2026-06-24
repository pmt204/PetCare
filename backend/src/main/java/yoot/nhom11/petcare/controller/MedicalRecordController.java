package yoot.nhom11.petcare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordListResponse;
import yoot.nhom11.petcare.service.MedicalRecordService;
import yoot.nhom11.petcare.util.SortValidator;

import java.util.Set;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "medicalRecordId", "date", "diagnosis", "createAt", "updateAt"
    );

    private final MedicalRecordService medicalRecordService;

    @GetMapping
    public ResponseEntity<Page<MedicalRecordListResponse>> getAllMedicalRecords(
            MedicalRecordFilterRequest filter,
            @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        SortValidator.validateSort(pageable, ALLOWED_SORT_FIELDS);
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDetailResponse> getMedicalRecordDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordDetail(id));
    }
}
