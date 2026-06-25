package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "prescription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prescriptionId;

    @Column(name = "quantity")
    private Integer quantity;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

}
