package yoot.nhom11.petcare.dto.request;

import java.time.LocalDate;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordFilterRequest {
    private String search;
    private Integer petId;
    private String billStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;
}
