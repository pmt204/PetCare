package yoot.nhom11.petcare.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private AppUser owner;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "species", nullable = false, length = 30)
	private PetSpecies species;

	@Column(name = "breed", length = 100)
	private String breed;

	@Column(name = "avatar_url", length = 500)
	private String avatarUrl;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "gender", length = 50)
	private String gender;

	@Builder.Default
	@OneToMany(mappedBy = "pet")
	private List<MedicalRecord> medicalRecords = new ArrayList<>();
}
