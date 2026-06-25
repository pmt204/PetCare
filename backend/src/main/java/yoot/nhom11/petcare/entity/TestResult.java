package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "result", columnDefinition = "text")
    private String result;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    // Fields for tai/admin:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

    @Column(name = "test_type")
    private String testType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "status", length = 20)
    private String status;

    // Compatibility getter/setter for hoai's code:
    public Integer getTestResultId() {
        return id != null ? id.intValue() : null;
    }

    public void setTestResultId(Integer testResultId) {
        this.id = testResultId != null ? testResultId.longValue() : null;
    }
    public static class TestResultBuilder {
        public TestResultBuilder testResultId(int testResultId) {
            this.id = (long) testResultId;
            return this;
        }
    }
}
