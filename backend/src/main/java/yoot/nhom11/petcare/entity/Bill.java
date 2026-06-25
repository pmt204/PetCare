package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "Bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "status")
    private String status;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

}
