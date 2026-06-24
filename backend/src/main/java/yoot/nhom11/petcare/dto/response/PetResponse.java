package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {
    private Integer petId;
    private String petName;
    private String petType;
    private Integer petAge;
    private String petGender;
    private String petAvatar;
    private String slug;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
