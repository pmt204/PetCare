package yoot.nhom11.petcare.mapper;

import java.util.List;

import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.LabResult;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.Prescription;

public final class MedicalRecordMapper {

	private MedicalRecordMapper() {
	}

	public static MedicalRecordTimelineItemResponse toTimelineItem(MedicalRecord record) {
		return new MedicalRecordTimelineItemResponse(
				record.getId(),
				record.getVisitAt(),
				record.getStatus(),
				record.getDiagnosis(),
				record.getReasonForVisit(),
				record.getVeterinarian().getFullName(),
				record.getPrescriptions().size(),
				record.getLabResults().size()
		);
	}

	public static MedicalRecordDetailResponse toDetailResponse(MedicalRecord record) {
		return new MedicalRecordDetailResponse(
				record.getId(),
				toPetSummary(record.getPet()),
				toUserSummary(record.getVeterinarian()),
				record.getVisitAt(),
				record.getStatus(),
				record.getReasonForVisit(),
				record.getDiagnosis(),
				record.getTreatmentNote(),
				record.getFollowUpInstruction(),
				record.getNextVisitDate(),
				toPrescriptionResponses(record.getPrescriptions()),
				toLabResultResponses(record.getLabResults())
		);
	}

	public static MedicalRecordDetailResponse.PetSummary toPetSummary(Pet pet) {
		return new MedicalRecordDetailResponse.PetSummary(
				pet.getId(),
				pet.getName(),
				pet.getSpecies(),
				pet.getBreed(),
				pet.getAvatarUrl()
		);
	}

	public static MedicalRecordDetailResponse.UserSummary toUserSummary(AppUser user) {
		return new MedicalRecordDetailResponse.UserSummary(
				user.getId(),
				user.getFullName(),
				user.getEmail(),
				user.getRole().name()
		);
	}

	public static List<MedicalRecordDetailResponse.PrescriptionItem> toPrescriptionResponses(List<Prescription> prescriptions) {
		return prescriptions.stream()
				.map(prescription -> new MedicalRecordDetailResponse.PrescriptionItem(
						prescription.getId(),
						prescription.getMedicationName(),
						prescription.getDosage(),
						prescription.getFrequency(),
						prescription.getDurationDays(),
						prescription.getInstructions()
				))
				.toList();
	}

	public static List<MedicalRecordDetailResponse.LabResultItem> toLabResultResponses(List<LabResult> labResults) {
		return labResults.stream()
				.map(labResult -> new MedicalRecordDetailResponse.LabResultItem(
						labResult.getId(),
						labResult.getTitle(),
						labResult.getFileName(),
						labResult.getFileUrl(),
						labResult.getMimeType(),
						labResult.getNote()
				))
				.toList();
	}
}
