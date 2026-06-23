package yoot.nhom11.petcare.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yoot.nhom11.petcare.dto.request.MedicalRecordTimelineFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.dto.response.PageResponse;
import yoot.nhom11.petcare.service.impl.MedicalRecordServiceImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

	private final MedicalRecordServiceImpl medicalRecordService;

	@GetMapping("/pets/{petId}/medical-records")
	public PageResponse<MedicalRecordTimelineItemResponse> getPetTimeline(
			@PathVariable Long petId,
			@Valid MedicalRecordTimelineFilterRequest request
	) {
		return medicalRecordService.getPetTimeline(petId, request);
	}

	@GetMapping("/medical-records/{recordId}")
	public MedicalRecordDetailResponse getMedicalRecordDetail(
			@PathVariable Long recordId
	) {
		return medicalRecordService.getMedicalRecordDetail(recordId);
	}
}
