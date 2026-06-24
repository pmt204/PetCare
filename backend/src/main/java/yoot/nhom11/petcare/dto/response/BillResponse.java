package yoot.nhom11.petcare.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {
    private Integer billId;
    private Double totalPrice;
    private String status;
}
