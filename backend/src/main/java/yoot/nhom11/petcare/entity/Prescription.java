package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;

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

	@Column(name = "medication_name", length = 150)
	private String medicationName;

	@Column(name = "dosage", length = 100)
	private String dosage;

	@Column(name = "frequency", length = 100)
	private String frequency;

	@Column(name = "duration_days")
	private Integer durationDays;

	@Column(name = "instructions", length = 1000)
	private String instructions;

	@Column(name = "quantity")
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medicine_id")
	private Medicine medicine;

	// Audit fields for hoai's code:
	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	public Integer getPrescriptionId() {
		return getId() != null ? getId().intValue() : null;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		if (prescriptionId != null) {
			setId(prescriptionId.longValue());
		} else {
			setId(null);
		}
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

	// Fields for tai/admin:
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@Column(name = "patient_name")
	private String patientName;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "medicine_list", columnDefinition = "text")
	private String medicineList;

	@Column(name = "status", length = 50)
	private String status;
}
