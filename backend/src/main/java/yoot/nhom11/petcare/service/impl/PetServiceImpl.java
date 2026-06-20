package yoot.nhom11.petcare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.mapper.PetMapper;
import yoot.nhom11.petcare.repository.PetRepository;
import yoot.nhom11.petcare.service.PetService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> getAllPets() {
        return petRepository.findAll().stream()
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetById(Integer id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        return petMapper.toPetResponse(pet);
    }

    @Override
    public PetResponse createPet(PetRequest request) {
        Pet pet = petMapper.toPet(request);
        LocalDateTime now = LocalDateTime.now();
        pet.setCreate_at(now);
        pet.setUpdate_at(now);
        Pet savedPet = petRepository.save(pet);
        return petMapper.toPetResponse(savedPet);
    }

    @Override
    public PetResponse updatePet(Integer id, PetRequest request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        
        petMapper.updatePetFromRequest(request, pet);
        pet.setUpdate_at(LocalDateTime.now());
        
        Pet updatedPet = petRepository.save(pet);
        return petMapper.toPetResponse(updatedPet);
    }
}
