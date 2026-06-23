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
        return p;
    }

    public static PrescriptionResponse toResponse(Prescription p) {
        PrescriptionResponse r = new PrescriptionResponse();
        r.setId(p.getId());
        r.setDoctorId(p.getDoctor().getId());
        r.setDoctorName(p.getDoctor().getName());
        r.setPatientName(p.getPatientName());
        r.setCreatedDate(p.getCreatedDate());
        r.setMedicineList(p.getMedicineList());
        r.setInstructions(p.getInstructions());
        r.setStatus(p.getStatus());
        return r;
    }
}