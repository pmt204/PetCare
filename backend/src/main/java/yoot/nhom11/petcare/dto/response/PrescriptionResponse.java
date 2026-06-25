package yoot.nhom11.petcare.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {
    private Integer prescriptionId;
    private Integer quantity;
    private Integer medicineId;
    private String medicineName;
    private String unit;
    private String description;
}
