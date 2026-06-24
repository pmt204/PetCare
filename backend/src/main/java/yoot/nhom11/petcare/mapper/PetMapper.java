package yoot.nhom11.petcare.mapper;

import org.springframework.stereotype.Component;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;

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

        return Pet.builder()
                .petName(request.getPetName())
                .petType(request.getPetType())
                .petAge(request.getPetAge())
                .petGender(request.getPetGender())
                .petAvatar(request.getPetAvatar())
                .build();
    }

    public void updatePetFromRequest(PetRequest request, Pet pet) {
        if (request == null || pet == null) {
            return;
        }

        pet.setPetName(request.getPetName());
        pet.setPetType(request.getPetType());
        pet.setPetAge(request.getPetAge());
        pet.setPetGender(request.getPetGender());
        pet.setPetAvatar(request.getPetAvatar());
    }
}
