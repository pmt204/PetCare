package yoot.nhom11.petcare.mapper;

import org.springframework.stereotype.Component;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.PetSpecies;

@Component
public class PetMapper {

    public PetResponse toPetResponse(Pet pet) {
        if (pet == null) {
            return null;
        }

        return PetResponse.builder()
                .petId(pet.getPetId())
                .petName(pet.getPetName())
                .petType(pet.getPetType())
                .petBreed(pet.getBreed())
                .petAge(pet.getPetAge())
                .petGender(pet.getPetGender())
                .petAvatar(pet.getPetAvatar())
                .slug(pet.getSlug())
                .createAt(pet.getCreateAt())
                .updateAt(pet.getUpdateAt())
                .build();
    }

    public Pet toPet(PetRequest request) {
        if (request == null) {
            return null;
        }

        PetSpecies species = null;
        if (request.getPetType() != null) {
            try {
                species = PetSpecies.valueOf(request.getPetType().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Fallback
            }
        }

        return Pet.builder()
                .name(request.getPetName())
                .species(species)
                .breed(request.getPetBreed())
                .petAge(request.getPetAge())
                .gender(request.getPetGender())
                .avatarUrl(request.getPetAvatar())
                .build();
    }

    public void updatePetFromRequest(PetRequest request, Pet pet) {
        if (request == null || pet == null) {
            return;
        }

        pet.setPetName(request.getPetName());
        pet.setPetType(request.getPetType());
        pet.setBreed(request.getPetBreed());
        pet.setPetAge(request.getPetAge());
        pet.setPetGender(request.getPetGender());
        pet.setPetAvatar(request.getPetAvatar());
    }
}
