package yoot.nhom11.petcare.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import yoot.nhom11.petcare.entity.PetSpecies;
import yoot.nhom11.petcare.entity.MedicalRecordStatus;

public record MedicalRecordDetailResponse(
		Long id,
		PetSummary pet,
		UserSummary veterinarian,
		Instant visitAt,
		MedicalRecordStatus status,
		String reasonForVisit,
		String diagnosis,
		String treatmentNote,
		String followUpInstruction,
		LocalDate nextVisitDate,
		List<PrescriptionItem> prescriptions,
		List<LabResultItem> labResults
) {

	public record PetSummary(
			Long id,
			String name,
			PetSpecies species,
			String breed,
			String avatarUrl
	) {
	}

	public record UserSummary(
			Long id,
			String fullName,
			String email,
			String role
	) {
	}

	public record PrescriptionItem(
			Long id,
			String medicationName,
			String dosage,
			String frequency,
			Integer durationDays,
			String instructions
	) {
	}

	public record LabResultItem(
			Long id,
			String title,
			String fileName,
			String fileUrl,
			String mimeType,
			String note
	) {
	}
}
