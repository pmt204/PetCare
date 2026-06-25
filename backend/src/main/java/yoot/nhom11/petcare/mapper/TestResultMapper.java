package yoot.nhom11.petcare.mapper;

import java.time.LocalDateTime;

import yoot.nhom11.petcare.dto.request.TestResultRequest;
import yoot.nhom11.petcare.dto.response.TestResultResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.TestResult;

public class TestResultMapper {

    public static TestResult toEntity(TestResultRequest r, Doctor doctor) {
        TestResult tr = new TestResult();
        tr.setDoctor(doctor);
        tr.setPatientName(r.getPatientName());
        tr.setTestType(r.getTestType());
        tr.setFilePath(r.getFilePath());
        tr.setResult(r.getResult());
        tr.setUploadedDate(LocalDateTime.now());
        return tr;
    }

    public static TestResultResponse toResponse(TestResult tr) {
        TestResultResponse r = new TestResultResponse();
        r.setId(tr.getId());
        r.setDoctorId(tr.getDoctor() != null ? tr.getDoctor().getId() : null);
        r.setDoctorName(tr.getDoctor() != null ? tr.getDoctor().getName() : null);
        r.setPatientName(tr.getPatientName());
        r.setUploadedDate(tr.getUploadedDate());
        r.setTestType(tr.getTestType());
        r.setFilePath(tr.getFilePath());
        r.setResult(tr.getResult());
        r.setStatus(tr.getStatus());
        return r;
    }
}