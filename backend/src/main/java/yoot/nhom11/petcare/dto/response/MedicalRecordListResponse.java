package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordListResponse {
    private Integer medicalRecordId;
    private Date date;
    private String diagnosis;
    private String treatment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Integer petId;
    private String petName;

    private Integer billId;
    private Double totalPrice;
    private String billStatus;
}
