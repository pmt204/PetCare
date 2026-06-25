package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "Test_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testResultId;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "result")
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
    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

}
