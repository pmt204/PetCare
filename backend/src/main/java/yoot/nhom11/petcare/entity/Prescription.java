package yoot.nhom11.petcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "medical_record_id", nullable = false)
	private MedicalRecord medicalRecord;

	@Column(name = "medication_name", nullable = false, length = 150)
	private String medicationName;

	@Column(name = "dosage", length = 100)
	private String dosage;

	@Column(name = "frequency", length = 100)
	private String frequency;

	@Column(name = "duration_days")
	private Integer durationDays;

	@Column(name = "instructions", length = 1000)
	private String instructions;
}
