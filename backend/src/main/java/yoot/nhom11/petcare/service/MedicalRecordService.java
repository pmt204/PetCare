package yoot.nhom11.petcare.service;

import java.util.List;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    MedicalRecordResponse getById(Long id);
    List<MedicalRecordResponse> listAll();
    MedicalRecordResponse update(Long id, MedicalRecordRequest request);
    void delete(Long id);
}
