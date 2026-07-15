package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.PetServiceRequest;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;

import java.util.List;

public interface PetServiceService {
    PetServiceResponse create(PetServiceRequest request);
    PetServiceResponse getById(Long id);
    List<PetServiceResponse> listAll();
    PetServiceResponse update(Long id, PetServiceRequest request);
    void delete(Long id);
}
