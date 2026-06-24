package yoot.nhom11.petcare.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private AppUser owner;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "species", length = 30)
	private PetSpecies species;

	@Column(name = "breed", length = 100)
	private String breed;

	@Column(name = "avatar_url", length = 500)
	private String avatarUrl;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "gender", length = 50)
	private String gender;

	@Column(name = "slug", unique = true, length = 150)
	private String slug;

	@Builder.Default
	@OneToMany(mappedBy = "pet")
	private List<MedicalRecord> medicalRecords = new ArrayList<>();

	// Aliases for hoai's code compatibility:
	public Integer getPetId() {
		return getId() != null ? getId().intValue() : null;
	}

	public void setPetId(Integer petId) {
		if (petId != null) {
			setId(petId.longValue());
		} else {
			setId(null);
		}
	}

	public String getPetName() {
		return name;
	}

	public void setPetName(String petName) {
		this.name = petName;
	}

	public String getPetType() {
		return species != null ? species.name() : null;
	}

	public void setPetType(String petType) {
		try {
			this.species = petType != null ? PetSpecies.valueOf(petType.toUpperCase()) : null;
		} catch (IllegalArgumentException e) {
			this.species = null;
		}
	}

	public String getPetAvatar() {
		return avatarUrl;
	}

	public void setPetAvatar(String petAvatar) {
		this.avatarUrl = petAvatar;
	}

	public String getPetGender() {
		return gender;
	}

	public void setPetGender(String petGender) {
		this.gender = petGender;
	}

	@Column(name = "pet_age")
	private Integer petAge;

	public LocalDateTime getCreateAt() {
		return getCreatedAt() != null ? LocalDateTime.ofInstant(getCreatedAt(), java.time.ZoneId.systemDefault()) : null;
	}

	public void setCreateAt(LocalDateTime createAt) {
		if (createAt != null) {
			setCreatedAt(createAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
		}
	}

	public LocalDateTime getUpdateAt() {
		return getUpdatedAt() != null ? LocalDateTime.ofInstant(getUpdatedAt(), java.time.ZoneId.systemDefault()) : null;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		if (updateAt != null) {
			setUpdatedAt(updateAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
		}
	}
}
