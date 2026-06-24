package yoot.nhom11.petcare.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordListResponse;

public interface MedicalRecordService {
    Page<MedicalRecordListResponse> getAllMedicalRecords(MedicalRecordFilterRequest filter, Pageable pageable);
    MedicalRecordDetailResponse getMedicalRecordDetail(Integer id);
}
