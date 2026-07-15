package yoot.nhom11.petcare.entity;

import java.time.Instant;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
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
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "veterinarian_id")
	private AppUser veterinarian;

	@Column(name = "visit_at")
	private Instant visitAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20)
	private MedicalRecordStatus status;

	@Column(name = "reason_for_visit", length = 500)
	private String reasonForVisit;

	@Column(name = "diagnosis", length = 1000)
	private String diagnosis;

	@Column(name = "treatment_note", length = 2000)
	private String treatmentNote;

	@Column(name = "follow_up_instruction", length = 1000)
	private String followUpInstruction;

	@Column(name = "next_visit_date")
	private LocalDate nextVisitDate;

	@Builder.Default
	@OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Prescription> prescriptions = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestResult> testResults = new ArrayList<>();

	@OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
	private Invoice invoice;

	// Fields for tai/admin:
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@Column(name = "patient_name")
	private String patientName;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "symptoms", columnDefinition = "text")
	private String symptoms;

	@Column(name = "notes", columnDefinition = "text")
	private String notes;

	// Aliases for hoai's code:
	public Integer getMedicalRecordId() {
		return getId() != null ? getId().intValue() : null;
	}

	public void setMedicalRecordId(Integer medicalRecordId) {
		if (medicalRecordId != null) {
			setId(medicalRecordId.longValue());
		} else {
			setId(null);
		}
	}

	public java.util.Date getDate() {
		return visitAt != null ? java.util.Date.from(visitAt) : null;
	}

	public void setDate(java.util.Date date) {
		this.visitAt = date != null ? date.toInstant() : null;
	}

	public String getTreatment() {
		return treatmentNote;
	}

	public void setTreatment(String treatment) {
		this.treatmentNote = treatment;
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

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;
	public static class MedicalRecordBuilder {
		private Long id;
		private java.time.Instant createdAt;
		private java.time.Instant updatedAt;

		private Pet pet;
		private AppUser veterinarian;
		private Instant visitAt;
		private MedicalRecordStatus status;
		private String reasonForVisit;
		private String diagnosis;
		private String treatmentNote;
		private String followUpInstruction;
		private LocalDate nextVisitDate;
		private List<Prescription> prescriptions;
		private List<TestResult> testResults;
		private Invoice invoice;
		private Doctor doctor;
		private String patientName;
		private LocalDateTime createdDate;
		private String symptoms;
		private String notes;
		private String createBy;
		private String updateBy;

		public MedicalRecordBuilder medicalRecordId(int medicalRecordId) {
			this.id = (long) medicalRecordId;
			return this;
		}

		public MedicalRecordBuilder prescriptions(List<Prescription> prescriptions) {
			this.prescriptions = prescriptions;
			return this;
		}

		public MedicalRecordBuilder testResults(List<TestResult> testResults) {
			this.testResults = testResults;
			return this;
		}

		public MedicalRecordBuilder invoice(Invoice invoice) {
			this.invoice = invoice;
			return this;
		}

		public MedicalRecordBuilder date(java.util.Date date) {
			if (date != null) {
				this.visitAt = date.toInstant();
			}
			return this;
		}

		public MedicalRecordBuilder treatment(String treatment) {
			this.treatmentNote = treatment;
			return this;
		}

		public MedicalRecordBuilder createAt(LocalDateTime createAt) {
			if (createAt != null) {
				this.createdAt = createAt.atZone(java.time.ZoneId.systemDefault()).toInstant();
			}
			return this;
		}

		public MedicalRecordBuilder updateAt(LocalDateTime updateAt) {
			if (updateAt != null) {
				this.updatedAt = updateAt.atZone(java.time.ZoneId.systemDefault()).toInstant();
			}
			return this;
		}

		public MedicalRecord build() {
			MedicalRecord record = new MedicalRecord(pet, veterinarian, visitAt, status, reasonForVisit, diagnosis, treatmentNote, followUpInstruction, nextVisitDate, prescriptions, testResults, invoice, doctor, patientName, createdDate, symptoms, notes, createBy, updateBy);
			record.setId(id);
			record.setCreatedAt(createdAt);
			record.setUpdatedAt(updatedAt);
			return record;
		}
	}
}
