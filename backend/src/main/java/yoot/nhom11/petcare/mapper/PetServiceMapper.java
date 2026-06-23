package yoot.nhom11.petcare.mapper;

import yoot.nhom11.petcare.dto.request.PetServiceRequest;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;
import yoot.nhom11.petcare.entity.PetService;

public class PetServiceMapper {

    public static PetService toEntity(PetServiceRequest request) {
        PetService petService = new PetService();
        petService.setName(request.getName());
        petService.setDescription(request.getDescription());
        petService.setPrice(request.getPrice());
        return petService;
    }

    public static PetServiceResponse toResponse(PetService petService) {
        PetServiceResponse response = new PetServiceResponse();
        response.setId(petService.getId());
        response.setName(petService.getName());
        response.setDescription(petService.getDescription());
        response.setPrice(petService.getPrice());
        return response;
    }
}
