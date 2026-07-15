package yoot.nhom11.petcare.service;

import java.util.List;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.request.MedicalRecordTimelineFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.dto.response.PageResponse;

public interface MedicalRecordService {
    MedicalRecordDetailResponse getMedicalRecordDetail(Integer id);
    MedicalRecordDetailResponse getMedicalRecordDetail(Long id);
    PageResponse<MedicalRecordTimelineItemResponse> getPetTimeline(Long petId, MedicalRecordTimelineFilterRequest request);

    MedicalRecordResponse create(MedicalRecordRequest request);
    MedicalRecordResponse getById(Long id);
    List<MedicalRecordResponse> listAll();
    MedicalRecordResponse update(Long id, MedicalRecordRequest request);
    void delete(Long id);
}
