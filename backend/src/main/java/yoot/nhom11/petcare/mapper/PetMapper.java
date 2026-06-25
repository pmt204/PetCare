package yoot.nhom11.petcare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PetMapper {

    PetResponse toPetResponse(Pet pet);

    Pet toPet(PetRequest request);

    void updatePetFromRequest(PetRequest request, @MappingTarget Pet pet);
}

