package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "Medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer medicalRecordId;

    @Column(name = "date")
    private Date date;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

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
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private Bill bill;

}
