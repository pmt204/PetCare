package yoot.nhom11.petcare.dto.request;

import java.time.LocalDate;

public class MedicalRecordRequest {
    private Long doctorId;
    private Long petId;
    private Long veterinarianId;
    private Long appointmentId;
    private String patientName;
    private String diagnosis;
    private String symptoms;
    private String notes;
    private String treatmentNote;
    private String followUpInstruction;
    private LocalDate nextVisitDate;
    private String reasonForVisit;

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public Long getVeterinarianId() { return veterinarianId; }
    public void setVeterinarianId(Long veterinarianId) { this.veterinarianId = veterinarianId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getTreatmentNote() { return treatmentNote; }
    public void setTreatmentNote(String treatmentNote) { this.treatmentNote = treatmentNote; }

    public String getFollowUpInstruction() { return followUpInstruction; }
    public void setFollowUpInstruction(String followUpInstruction) { this.followUpInstruction = followUpInstruction; }

    public LocalDate getNextVisitDate() { return nextVisitDate; }
    public void setNextVisitDate(LocalDate nextVisitDate) { this.nextVisitDate = nextVisitDate; }

    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }
}
