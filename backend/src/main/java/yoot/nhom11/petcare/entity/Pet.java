package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "Pet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer petId;

    @Column(name = "pet_avatar")
    private String petAvatar;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_type")
    private String petType;

    @Column(name = "pet_age")
    private Integer petAge;

    @Column(name = "pet_gender")
    private String petGender;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords;

}
