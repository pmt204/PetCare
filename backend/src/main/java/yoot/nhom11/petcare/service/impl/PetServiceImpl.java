package yoot.nhom11.petcare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yoot.nhom11.petcare.dto.request.PetFilterRequest;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.mapper.PetMapper;
import yoot.nhom11.petcare.repository.PetRepository;
import yoot.nhom11.petcare.repository.specification.PetSpecification;
import yoot.nhom11.petcare.service.PetService;
import yoot.nhom11.petcare.util.SlugUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final yoot.nhom11.petcare.repository.AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PetResponse> getAllPets(PetFilterRequest filter, Pageable pageable) {
        return petRepository.findAll(PetSpecification.filterPets(filter), pageable)
                .map(petMapper::toPetResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetById(Integer id) {
        Pet pet = petRepository.findById(id.longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        return petMapper.toPetResponse(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetBySlug(String slug) {
        Pet pet = petRepository.findBySlug(slug)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found with slug: " + slug));
        return petMapper.toPetResponse(pet);
    }

    @Override
    public PetResponse createPet(PetRequest request) {
        Pet pet = petMapper.toPet(request);
        LocalDateTime now = LocalDateTime.now();
        pet.setCreateAt(now);
        pet.setUpdateAt(now);
        pet.setSlug(generateUniqueSlug(pet.getPetName(), null));

        Long ownerId = request.getOwnerId();
        if (ownerId == null) {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof yoot.nhom11.petcare.security.UserDetailsImpl) {
                ownerId = ((yoot.nhom11.petcare.security.UserDetailsImpl) auth.getPrincipal()).getId();
            }
        }

        if (ownerId != null && appUserRepository != null) {
            yoot.nhom11.petcare.entity.AppUser owner = appUserRepository.findById(ownerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
            pet.setOwner(owner);
        } else if (appUserRepository != null) {
            appUserRepository.findAll().stream().findFirst().ifPresent(pet::setOwner);
        }

        Pet savedPet = petRepository.save(pet);
        return petMapper.toPetResponse(savedPet);
    }

    @Override
    public PetResponse updatePet(Integer id, PetRequest request) {
        Pet pet = petRepository.findById(id.longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

        String oldName = pet.getPetName();
        petMapper.updatePetFromRequest(request, pet);

        if (!pet.getPetName().equalsIgnoreCase(oldName) || pet.getSlug() == null) {
            pet.setSlug(generateUniqueSlug(pet.getPetName(), id));
        }

        pet.setUpdateAt(LocalDateTime.now());

        Pet updatedPet = petRepository.save(pet);
        return petMapper.toPetResponse(updatedPet);
    }

    @Override
    public void deletePet(Integer id) {
        Pet pet = petRepository.findById(id.longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        petRepository.delete(pet);
    }

    private String generateUniqueSlug(String name, Integer currentId) {
        String baseSlug = SlugUtils.slugify(name);
        if (baseSlug == null || baseSlug.isEmpty()) {
            baseSlug = "pet";
        }
        String slug = baseSlug;
        int count = 1;
        while (true) {
            Optional<Pet> existing = petRepository.findBySlug(slug);
            if (existing.isEmpty() || (currentId != null && existing.get().getPetId().equals(currentId))) {
                return slug;
            }
            slug = baseSlug + "-" + count++;
        }
    }
}
