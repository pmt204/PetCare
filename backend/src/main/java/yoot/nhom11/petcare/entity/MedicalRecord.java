package yoot.nhom11.petcare.entity;

import java.time.Instant;
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
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pet_id", nullable = false)
	private Pet pet;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "veterinarian_id", nullable = false)
	private AppUser veterinarian;

	@Column(name = "visit_at", nullable = false)
	private Instant visitAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private MedicalRecordStatus status;

	@Column(name = "reason_for_visit", length = 500)
	private String reasonForVisit;

	@Column(name = "diagnosis", nullable = false, length = 1000)
	private String diagnosis;

	@Column(name = "treatment_note", length = 2000)
	private String treatmentNote;

	@Column(name = "follow_up_instruction", length = 1000)
	private String followUpInstruction;

	@Column(name = "next_visit_date")
	private LocalDate nextVisitDate;

	@Builder.Default
	@OneToMany(mappedBy = "medicalRecord")
	private List<Prescription> prescriptions = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "medicalRecord")
	private List<LabResult> labResults = new ArrayList<>();
}
