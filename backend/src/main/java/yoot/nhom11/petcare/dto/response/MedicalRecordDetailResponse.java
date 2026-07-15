package yoot.nhom11.petcare.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import yoot.nhom11.petcare.entity.PetSpecies;
import yoot.nhom11.petcare.entity.MedicalRecordStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDetailResponse {
    // hoai's fields
    private ExaminationResponse examination;
    private List<PrescriptionResponse> prescriptions;
    private List<TestResultResponse> testResults;
    private BillResponse bill;

    // HEAD's fields
    private Long id;
    private PetSummary pet;
    private UserSummary veterinarian;
    private Instant visitAt;
    private MedicalRecordStatus status;
    private String reasonForVisit;
    private String diagnosis;
    private String treatmentNote;
    private String followUpInstruction;
    private LocalDate nextVisitDate;
    private List<PrescriptionItem> prescriptionItems;
    private List<LabResultItem> labResults;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PetSummary {
        private Long id;
        private String name;
        private PetSpecies species;
        private String breed;
        private String avatarUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummary {
        private Long id;
        private String fullName;
        private String email;
        private String role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PrescriptionItem {
        private Long id;
        private String medicationName;
        private String dosage;
        private String frequency;
        private Integer durationDays;
        private String instructions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LabResultItem {
        private Long id;
        private String title;
        private String fileName;
        private String fileUrl;
        private String mimeType;
        private String note;
    }
}
