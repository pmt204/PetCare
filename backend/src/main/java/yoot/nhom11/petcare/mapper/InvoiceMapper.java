package yoot.nhom11.petcare.mapper;

import yoot.nhom11.petcare.dto.response.InvoiceResponse;
import yoot.nhom11.petcare.entity.Invoice;

import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        if (invoice.getAppointment() != null) {
            response.setAppointment(AppointmentMapper.toResponse(invoice.getAppointment()));
        }
        if (invoice.getServices() != null) {
            response.setServices(invoice.getServices().stream()
                    .map(PetServiceMapper::toResponse)
                    .collect(Collectors.toList()));
        }
        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaymentStatus(invoice.getPaymentStatus());
        response.setCreatedAt(invoice.getCreatedAt());
        return response;
    }
}
