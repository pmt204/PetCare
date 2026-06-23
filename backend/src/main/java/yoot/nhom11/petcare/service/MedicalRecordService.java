package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;

public interface MedicalRecordService {
    MedicalRecordDetailResponse getMedicalRecordDetail(Integer id);
}
