package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;

<<<<<<< HEAD
import yoot.nhom11.petcare.entity.AppointmentStatus;
import yoot.nhom11.petcare.entity.PetSpecies;

public record AppointmentResponse(
		Long id,
		OwnerSummary owner,
		PetSummary pet,
		VeterinarianSummary veterinarian,
		LocalDateTime appointmentAt,
		String reasonForVisit,
		AppointmentStatus status
) {
	public record OwnerSummary(Long id, String fullName, String email) {
	}

	public record PetSummary(Long id, String name, PetSpecies species, String breed) {
	}

	public record VeterinarianSummary(Long id, String fullName, String email) {
	}
=======
public class AppointmentResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private String patientPhone;
    private LocalDateTime appointmentTime;
    private String reason;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
>>>>>>> origin/tai/admin
}
