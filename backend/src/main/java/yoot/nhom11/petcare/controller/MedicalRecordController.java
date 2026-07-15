package yoot.nhom11.petcare.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.request.MedicalRecordTimelineFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.dto.response.PageResponse;
import yoot.nhom11.petcare.service.MedicalRecordService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

	private final MedicalRecordService medicalRecordService;

	@GetMapping("/pets/{petId}/medical-records")
	public PageResponse<MedicalRecordTimelineItemResponse> getPetTimeline(
			@PathVariable Long petId,
			@Valid MedicalRecordTimelineFilterRequest request
	) {
		return medicalRecordService.getPetTimeline(petId, request);
	}

	@GetMapping("/medical-records/{recordId}")
	public ResponseEntity<MedicalRecordDetailResponse> getMedicalRecordDetail(
			@PathVariable String recordId
	) {
		try {
			Integer intId = Integer.parseInt(recordId);
			return ResponseEntity.ok(medicalRecordService.getMedicalRecordDetail(intId));
		} catch (NumberFormatException e) {
			Long longId = Long.parseLong(recordId);
			return ResponseEntity.ok(medicalRecordService.getMedicalRecordDetail(longId));
		}
	}

	@PostMapping("/medical-records")
	public ResponseEntity<MedicalRecordResponse> create(@RequestBody MedicalRecordRequest request) {
		return ResponseEntity.ok(medicalRecordService.create(request));
	}
}
