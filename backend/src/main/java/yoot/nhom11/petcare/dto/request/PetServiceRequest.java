package yoot.nhom11.petcare.dto.request;

import lombok.Data;

@Data
public class PetServiceRequest {
    private String name;
    private String description;
    private double price;
}
