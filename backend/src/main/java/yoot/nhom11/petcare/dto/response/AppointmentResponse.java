package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;
import yoot.nhom11.petcare.entity.AppointmentStatus;
import yoot.nhom11.petcare.entity.PetSpecies;

public class AppointmentResponse {
    // Record-style fields
    private Long id;
    private OwnerSummary owner;
    private PetSummary pet;
    private VeterinarianSummary veterinarian;
    private LocalDateTime appointmentAt;
    private String reasonForVisit;
    private AppointmentStatus appointmentStatus;

    // Class-style fields
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private String patientPhone;
    private LocalDateTime appointmentTime;
    private String reason;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentUrl;

    public record OwnerSummary(Long id, String fullName, String email, String phone) {}
    public record PetSummary(Long id, String name, PetSpecies species, String breed) {}
    public record VeterinarianSummary(Long id, String fullName, String email) {}

    // Default constructor
    public AppointmentResponse() {
    }

    // Record-style constructor
    public AppointmentResponse(
            Long id,
            OwnerSummary owner,
            PetSummary pet,
            VeterinarianSummary veterinarian,
            LocalDateTime appointmentAt,
            String reasonForVisit,
            AppointmentStatus appointmentStatus
    ) {
        this.id = id;
        this.owner = owner;
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.appointmentAt = appointmentAt;
        this.reasonForVisit = reasonForVisit;
        this.appointmentStatus = appointmentStatus;
    }

    // Record-style getters (field name as method name)
    public Long id() { return id; }
    public OwnerSummary owner() { return owner; }
    public PetSummary pet() { return pet; }
    public VeterinarianSummary veterinarian() { return veterinarian; }
    public LocalDateTime appointmentAt() { return appointmentAt; }
    public String reasonForVisit() { return reasonForVisit; }
    public AppointmentStatus statusEnum() { return appointmentStatus; }

    // Standard getters/setters for class-style mapping
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OwnerSummary getOwner() { return owner; }
    public void setOwner(OwnerSummary owner) { this.owner = owner; }

    public PetSummary getPet() { return pet; }
    public void setPet(PetSummary pet) { this.pet = pet; }

    public VeterinarianSummary getVeterinarian() { return veterinarian; }
    public void setVeterinarian(VeterinarianSummary veterinarian) { this.veterinarian = veterinarian; }

    public LocalDateTime getAppointmentAt() { return appointmentAt; }
    public void setAppointmentAt(LocalDateTime appointmentAt) { this.appointmentAt = appointmentAt; }

    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }

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

    // Dynamic getter for status
    public String getStatus() {
        if (status != null) {
            return status;
        }
        return appointmentStatus != null ? appointmentStatus.name() : null;
    }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentUrl() { return paymentUrl; }
    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
}
