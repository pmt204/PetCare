package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.MedicalRecordTimelineFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.dto.response.PageResponse;

public interface MedicalRecordService {
    MedicalRecordDetailResponse getMedicalRecordDetail(Integer id);
    MedicalRecordDetailResponse getMedicalRecordDetail(Long id);
    PageResponse<MedicalRecordTimelineItemResponse> getPetTimeline(Long petId, MedicalRecordTimelineFilterRequest request);
}
