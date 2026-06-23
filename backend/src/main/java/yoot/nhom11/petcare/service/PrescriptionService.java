package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.PrescriptionRequest;
import yoot.nhom11.petcare.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse create(PrescriptionRequest request);
    PrescriptionResponse getById(Long id);
    List<PrescriptionResponse> listAll();
    PrescriptionResponse update(Long id, PrescriptionRequest request);
    void delete(Long id);
}
