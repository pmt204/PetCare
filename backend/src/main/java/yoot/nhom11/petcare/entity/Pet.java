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

	@Column(name = "weight", length = 50)
	private String weight;

	public String getPetWeight() {
		return weight;
	}

	public void setPetWeight(String weight) {
		this.weight = weight;
	}


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
	public static class PetBuilder {
		private Long id;
		private java.time.Instant createdAt;
		private java.time.Instant updatedAt;

		private AppUser owner;
		private String name;
		private PetSpecies species;
		private String breed;
		private String avatarUrl;
		private LocalDate birthDate;
		private String gender;
		private String slug;
		private List<MedicalRecord> medicalRecords;
		private Integer petAge;
		private String weight;


		public PetBuilder petId(int petId) {
			this.id = (long) petId;
			return this;
		}

		public PetBuilder medicalRecords(List<MedicalRecord> medicalRecords) {
			this.medicalRecords = medicalRecords;
			return this;
		}

		public PetBuilder petName(String petName) {
			this.name = petName;
			return this;
		}

		public PetBuilder petType(String petType) {
			try {
				this.species = petType != null ? PetSpecies.valueOf(petType.toUpperCase()) : null;
			} catch (IllegalArgumentException e) {
				this.species = null;
			}
			return this;
		}

		public PetBuilder petAvatar(String petAvatar) {
			this.avatarUrl = petAvatar;
			return this;
		}

		public PetBuilder petGender(String petGender) {
			this.gender = petGender;
			return this;
		}

		public PetBuilder weight(String weight) {
			this.weight = weight;
			return this;
		}


		public PetBuilder createAt(LocalDateTime createAt) {
			if (createAt != null) {
				this.createdAt = createAt.atZone(java.time.ZoneId.systemDefault()).toInstant();
			}
			return this;
		}

		public PetBuilder updateAt(LocalDateTime updateAt) {
			if (updateAt != null) {
				this.updatedAt = updateAt.atZone(java.time.ZoneId.systemDefault()).toInstant();
			}
			return this;
		}

		public Pet build() {
			Pet pet = new Pet(owner, name, species, breed, avatarUrl, birthDate, gender, slug, medicalRecords, petAge, weight);
			pet.setId(id);
			pet.setCreatedAt(createdAt);
			pet.setUpdatedAt(updatedAt);
			return pet;
		}

	}
}
