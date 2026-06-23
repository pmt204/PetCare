package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.InvoiceRequest;
import yoot.nhom11.petcare.dto.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse create(InvoiceRequest request);
    InvoiceResponse getById(Long id);
    List<InvoiceResponse> listAll();
    InvoiceResponse updatePaymentStatus(Long id, InvoiceRequest request);
}
