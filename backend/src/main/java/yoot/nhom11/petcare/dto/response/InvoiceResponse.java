package yoot.nhom11.petcare.dto.response;

import lombok.Data;
import yoot.nhom11.petcare.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponse {
    private Long id;
    private AppointmentResponse appointment;
    private List<PetServiceResponse> services;
    private double totalAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}
