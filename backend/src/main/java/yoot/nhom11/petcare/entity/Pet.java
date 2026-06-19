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
    private Integer pet_id;

    @Column(name = "pet_avatar")
    private String pet_avatar;

    @Column(name = "pet_name")
    private String pet_name;

    @Column(name = "pet_type")
    private String pet_type;

    @Column(name = "pet_age")
    private Integer pet_age;

    @Column(name = "pet_gender")
    private String pet_gender;

    @Column(name = "create_at")
    private LocalDateTime create_at;

    @Column(name = "update_at")
    private LocalDateTime update_at;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medical_records;

}
