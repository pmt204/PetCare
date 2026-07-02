package yoot.nhom11.petcare.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "appointments")
public class Appointment extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private AppUser owner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "veterinarian_id")
	private AppUser veterinarian;

	@Column(name = "appointment_at")
	private LocalDateTime appointmentAt;

	@Column(name = "reason_for_visit", length = 1000)
	private String reasonForVisit;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20)
	private AppointmentStatus status;

	@Column(name = "payment_method", length = 50)
	private String paymentMethod;

	@Column(name = "payment_status", length = 50)
	private String paymentStatus;

	// Fields for tai/admin:
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@Column(name = "patient_name")
	private String patientName;

	@Column(name = "patient_phone")
	private String patientPhone;

	@Column(name = "appointment_time")
	private LocalDateTime appointmentTime;

	@Column(name = "reason")
	private String reason;

	// Compatibility getters/setters:
	public String getStatusStr() {
		return status != null ? status.name().toLowerCase() : "pending";
	}

	public void setStatusStr(String statusStr) {
		if (statusStr != null) {
			try {
				this.status = AppointmentStatus.valueOf(statusStr.toUpperCase());
			} catch (IllegalArgumentException e) {
				// Fallback
			}
		}
	}
}
