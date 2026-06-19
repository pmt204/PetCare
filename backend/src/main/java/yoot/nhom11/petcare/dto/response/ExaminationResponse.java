package yoot.nhom11.petcare.dto.response;

import java.util.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExaminationResponse {
    private Integer medicalRecordId;
    private Date date;
    private String diagnosis;
    private String treatment;
}
