package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {
    // hoai's fields
    private Integer prescriptionId;
    private Integer quantity;
    private Integer medicineId;
    private String medicineName;
    private String unit;
    private String description;

    // tai/admin's fields
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private LocalDateTime createdDate;
    private String medicineList;
    private String instructions;
    private String status;
}
