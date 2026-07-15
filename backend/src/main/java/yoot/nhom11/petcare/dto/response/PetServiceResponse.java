package yoot.nhom11.petcare.dto.response;

import lombok.Data;

@Data
public class PetServiceResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
}
