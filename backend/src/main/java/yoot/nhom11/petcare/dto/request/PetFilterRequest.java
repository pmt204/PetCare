package yoot.nhom11.petcare.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetFilterRequest {
    private String search;
    private String petType;
    private String petGender;
    private Integer minAge;
    private Integer maxAge;
}
