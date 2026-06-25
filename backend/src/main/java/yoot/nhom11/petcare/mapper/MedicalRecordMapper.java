package yoot.nhom11.petcare.mapper;

import org.springframework.stereotype.Component;
import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.response.*;
import yoot.nhom11.petcare.entity.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalRecordMapper {

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
		return MedicalRecordDetailResponse.builder()
				.id(record.getId())
				.pet(toPetSummary(record.getPet()))
				.veterinarian(toUserSummary(record.getVeterinarian()))
				.visitAt(record.getVisitAt())
				.status(record.getStatus())
				.reasonForVisit(record.getReasonForVisit())
				.diagnosis(record.getDiagnosis())
				.treatmentNote(record.getTreatmentNote())
				.followUpInstruction(record.getFollowUpInstruction())
				.nextVisitDate(record.getNextVisitDate())
				.prescriptionItems(toPrescriptionResponsesStatic(record.getPrescriptions()))
				.labResults(toLabResultResponses(record.getLabResults()))
				.build();
	}

	public static MedicalRecordDetailResponse.PetSummary toPetSummary(Pet pet) {
		if (pet == null) return null;
		return new MedicalRecordDetailResponse.PetSummary(
				pet.getId(),
				pet.getName(),
				pet.getSpecies(),
				pet.getBreed(),
				pet.getAvatarUrl()
		);
	}

	public static MedicalRecordDetailResponse.UserSummary toUserSummary(AppUser user) {
		if (user == null) return null;
		return new MedicalRecordDetailResponse.UserSummary(
				user.getId(),
				user.getFullName(),
				user.getEmail(),
				user.getRole().name()
		);
	}

	public static List<MedicalRecordDetailResponse.PrescriptionItem> toPrescriptionResponsesStatic(List<Prescription> prescriptions) {
		if (prescriptions == null) return Collections.emptyList();
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
		if (labResults == null) return Collections.emptyList();
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

    // Component-level mapping methods for hoai's branch:
    public MedicalRecordDetailResponse toMedicalRecordDetailResponse(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        ExaminationResponse examination = ExaminationResponse.builder()
                .medicalRecordId(medicalRecord.getMedicalRecordId())
                .date(medicalRecord.getDate())
                .diagnosis(medicalRecord.getDiagnosis())
                .treatment(medicalRecord.getTreatment())
                .build();

        List<PrescriptionResponse> prescriptions = medicalRecord.getPrescriptions() == null ? Collections.emptyList() :
                medicalRecord.getPrescriptions().stream()
                        .map(this::toPrescriptionResponse)
                        .collect(Collectors.toList());

        List<TestResultResponse> testResults = medicalRecord.getTestResults() == null ? Collections.emptyList() :
                medicalRecord.getTestResults().stream()
                        .map(this::toTestResultResponse)
                        .collect(Collectors.toList());

        BillResponse bill = toBillResponse(medicalRecord.getBill());

        return MedicalRecordDetailResponse.builder()
                .id(medicalRecord.getId())
                .examination(examination)
                .prescriptions(prescriptions)
                .testResults(testResults)
                .bill(bill)
                .pet(toPetSummary(medicalRecord.getPet()))
                .veterinarian(toUserSummary(medicalRecord.getVeterinarian()))
                .visitAt(medicalRecord.getVisitAt())
                .status(medicalRecord.getStatus())
                .reasonForVisit(medicalRecord.getReasonForVisit())
                .diagnosis(medicalRecord.getDiagnosis())
                .treatmentNote(medicalRecord.getTreatmentNote())
                .followUpInstruction(medicalRecord.getFollowUpInstruction())
                .nextVisitDate(medicalRecord.getNextVisitDate())
                .prescriptionItems(toPrescriptionResponsesStatic(medicalRecord.getPrescriptions()))
                .labResults(toLabResultResponses(medicalRecord.getLabResults()))
                .build();
    }

    public PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionResponse.PrescriptionResponseBuilder builder = PrescriptionResponse.builder()
                .prescriptionId(prescription.getPrescriptionId())
                .quantity(prescription.getQuantity());

        if (prescription.getMedicine() != null) {
            builder.medicineId(prescription.getMedicine().getMedicineId())
                   .medicineName(prescription.getMedicine().getMedicineName())
                   .unit(prescription.getMedicine().getUnit())
                   .description(prescription.getMedicine().getDescription());
        }

        return builder.build();
    }

    public TestResultResponse toTestResultResponse(TestResult testResult) {
        if (testResult == null) {
            return null;
        }

        return TestResultResponse.builder()
                .testResultId(testResult.getTestResultId())
                .testName(testResult.getTestName())
                .result(testResult.getResult())
                .pdfUrl(testResult.getPdfUrl())
                .build();
    }

    public BillResponse toBillResponse(Bill bill) {
        if (bill == null) {
            return null;
        }

        return BillResponse.builder()
                .billId(bill.getBillId())
                .totalPrice(bill.getTotalPrice())
                .status(bill.getStatus())
                .build();
    }

    // Static mapping methods for tai/admin's branch:
    public static MedicalRecord toEntity(MedicalRecordRequest r, Doctor doctor, Pet pet, AppUser veterinarian) {
        MedicalRecord mr = new MedicalRecord();
        mr.setDoctor(doctor);
        mr.setPet(pet);
        mr.setVeterinarian(veterinarian);
        mr.setPatientName(r.getPatientName());
        mr.setDiagnosis(r.getDiagnosis());
        mr.setSymptoms(r.getSymptoms());
        mr.setNotes(r.getNotes());
        mr.setTreatmentNote(r.getTreatmentNote() != null ? r.getTreatmentNote() : r.getNotes());
        mr.setFollowUpInstruction(r.getFollowUpInstruction());
        mr.setNextVisitDate(r.getNextVisitDate());
        mr.setReasonForVisit(r.getReasonForVisit());
        mr.setCreatedDate(LocalDateTime.now());
        mr.setVisitAt(Instant.now());
        mr.setStatus(MedicalRecordStatus.COMPLETED);
        return mr;
    }

    public static MedicalRecordResponse toResponse(MedicalRecord mr) {
        MedicalRecordResponse r = new MedicalRecordResponse();
        r.setId(mr.getId());
        r.setDoctorId(mr.getDoctor() != null ? mr.getDoctor().getId() : null);
        r.setDoctorName(mr.getDoctor() != null ? mr.getDoctor().getName() : null);
        r.setPatientName(mr.getPatientName());
        r.setCreatedDate(mr.getCreatedDate());
        r.setDiagnosis(mr.getDiagnosis());
        r.setSymptoms(mr.getSymptoms());
        r.setNotes(mr.getNotes());
        return r;
    }
}
