package yoot.nhom11.petcare.mapper;

import java.time.LocalDateTime;

import yoot.nhom11.petcare.dto.request.PrescriptionRequest;
import yoot.nhom11.petcare.dto.response.PrescriptionResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.Prescription;

public class PrescriptionMapper {

    public static Prescription toEntity(PrescriptionRequest r, Doctor doctor) {
        Prescription p = new Prescription();
        p.setDoctor(doctor);
        p.setPatientName(r.getPatientName());
        p.setMedicineList(r.getMedicineList());
        p.setInstructions(r.getInstructions());
        p.setCreatedDate(LocalDateTime.now());
        p.setStatus(r.getStatus() != null ? r.getStatus() : "Active");
        return p;
    }

    public static PrescriptionResponse toResponse(Prescription p) {
        PrescriptionResponse r = new PrescriptionResponse();
        r.setId(p.getId());
        
        // Null checks for Doctor relationship to avoid NPE 500 errors
        if (p.getDoctor() != null) {
            r.setDoctorId(p.getDoctor().getId());
            r.setDoctorName(p.getDoctor().getName());
        } else {
            r.setDoctorId(null);
            r.setDoctorName("Chưa chỉ định");
        }
        
        // Handling compatibility fields if they are null
        r.setPatientName(p.getPatientName() != null ? p.getPatientName() : 
                         (p.getMedicalRecord() != null && p.getMedicalRecord().getPet() != null ? p.getMedicalRecord().getPet().getName() : "Thú cưng"));
        
        r.setCreatedDate(p.getCreatedDate() != null ? p.getCreatedDate() : 
                         (p.getCreatedAt() != null ? LocalDateTime.ofInstant(p.getCreatedAt(), java.time.ZoneId.systemDefault()) : LocalDateTime.now()));
        
        r.setMedicineList(p.getMedicineList() != null ? p.getMedicineList() : 
                          (p.getMedicationName() != null ? p.getMedicationName() + " (" + p.getDosage() + " " + p.getFrequency() + ")" : "Không rõ"));
        
        r.setInstructions(p.getInstructions() != null ? p.getInstructions() : "Theo chỉ dẫn của bác sĩ");
        r.setStatus(p.getStatus() != null ? p.getStatus() : "Active");
        
        return r;
    }
}