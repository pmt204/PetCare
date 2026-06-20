package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;

import java.util.List;

public interface PetService {
    List<PetResponse> getAllPets();
    PetResponse getPetById(Integer id);
    PetResponse createPet(PetRequest request);
    PetResponse updatePet(Integer id, PetRequest request);
}
