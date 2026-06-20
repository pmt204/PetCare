package yoot.nhom11.petcare.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequest {

    @NotBlank(message = "Pet name is required")
    private String petName;

    @NotBlank(message = "Pet type is required")
    private String petType;

    @Min(value = 0, message = "Pet age must be greater than or equal to 0")
    private Integer petAge;

    private String petGender;

    private String petAvatar;
}
