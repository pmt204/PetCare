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
                .petId(pet.getPet_id())
                .petName(pet.getPet_name())
                .petType(pet.getPet_type())
                .petAge(pet.getPet_age())
                .petGender(pet.getPet_gender())
                .petAvatar(pet.getPet_avatar())
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
                .pet_name(request.getPetName())
                .pet_type(request.getPetType())
                .pet_age(request.getPetAge())
                .pet_gender(request.getPetGender())
                .pet_avatar(request.getPetAvatar())
                .build();
    }

    public void updatePetFromRequest(PetRequest request, Pet pet) {
        if (request == null || pet == null) {
            return;
        }

        pet.setPet_name(request.getPetName());
        pet.setPet_type(request.getPetType());
        pet.setPet_age(request.getPetAge());
        pet.setPet_gender(request.getPetGender());
        pet.setPet_avatar(request.getPetAvatar());
    }
}
