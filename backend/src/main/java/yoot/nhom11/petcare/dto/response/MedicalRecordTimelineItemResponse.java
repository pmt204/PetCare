package yoot.nhom11.petcare.dto.response;

import java.time.Instant;

import yoot.nhom11.petcare.entity.MedicalRecordStatus;

public record MedicalRecordTimelineItemResponse(
		Long id,
		Instant visitAt,
		MedicalRecordStatus status,
		String diagnosis,
		String reasonForVisit,
		String veterinarianName,
		int prescriptionCount,
		int labResultCount
) {
}
