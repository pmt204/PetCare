package yoot.nhom11.petcare.dto.request;

import lombok.Data;
import yoot.nhom11.petcare.entity.PaymentStatus;

import java.util.List;

@Data
public class InvoiceRequest {
    private Long appointmentId;
    private List<Long> serviceIds;
    private PaymentStatus paymentStatus;
}
