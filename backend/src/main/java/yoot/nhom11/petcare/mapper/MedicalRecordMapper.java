package yoot.nhom11.petcare.mapper;

import java.time.LocalDateTime;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.MedicalRecord;

public class MedicalRecordMapper {

    public static MedicalRecord toEntity(MedicalRecordRequest r, Doctor doctor) {
        MedicalRecord mr = new MedicalRecord();
        mr.setDoctor(doctor);
        mr.setPatientName(r.getPatientName());
        mr.setDiagnosis(r.getDiagnosis());
        mr.setSymptoms(r.getSymptoms());
        mr.setNotes(r.getNotes());
        mr.setCreatedDate(LocalDateTime.now());
        return mr;
    }

    public static MedicalRecordResponse toResponse(MedicalRecord mr) {
        MedicalRecordResponse r = new MedicalRecordResponse();
        r.setId(mr.getId());
        r.setDoctorId(mr.getDoctor().getId());
        r.setDoctorName(mr.getDoctor().getName());
        r.setPatientName(mr.getPatientName());
        r.setCreatedDate(mr.getCreatedDate());
        r.setDiagnosis(mr.getDiagnosis());
        r.setSymptoms(mr.getSymptoms());
        r.setNotes(mr.getNotes());
        return r;
    }
}