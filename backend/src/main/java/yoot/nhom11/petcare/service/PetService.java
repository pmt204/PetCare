package yoot.nhom11.petcare.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yoot.nhom11.petcare.dto.request.PetFilterRequest;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;

public interface PetService {
    Page<PetResponse> getAllPets(PetFilterRequest filter, Pageable pageable);
    PetResponse getPetById(Integer id);
    PetResponse getPetBySlug(String slug);
    PetResponse createPet(PetRequest request);
    PetResponse updatePet(Integer id, PetRequest request);
    void deletePet(Integer id);
}
