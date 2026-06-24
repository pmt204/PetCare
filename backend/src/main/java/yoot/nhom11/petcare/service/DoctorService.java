package yoot.nhom11.petcare.service;

import java.util.List;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.DoctorResponse;

public interface DoctorService {
    DoctorResponse create(DoctorRequest request);
    DoctorResponse getById(Long id);
    List<DoctorResponse> listAll();
    DoctorResponse update(Long id, DoctorRequest request);
    void delete(Long id);
}
