package yoot.nhom11.petcare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoot.nhom11.petcare.dto.request.InvoiceRequest;
import yoot.nhom11.petcare.dto.response.InvoiceResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Invoice;
import yoot.nhom11.petcare.entity.PaymentStatus;
import yoot.nhom11.petcare.entity.PetService;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.InvoiceRepository;
import yoot.nhom11.petcare.repository.PetServiceRepository;
import yoot.nhom11.petcare.service.impl.InvoiceServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PetServiceRepository petServiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    void createInvoice_shouldSaveAndReturnResponse() {
        InvoiceRequest request = new InvoiceRequest();
        request.setAppointmentId(1L);
        request.setServiceIds(Collections.singletonList(1L));
        request.setPaymentStatus(PaymentStatus.UNPAID);

        Appointment appointment = new Appointment();
        appointment.setId(1L);

        PetService petService = new PetService(1L, "Check-up", "Annual check-up", 50.0);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(petServiceRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.singletonList(petService));

        Invoice savedInvoice = new Invoice();
        savedInvoice.setId(1L);
        savedInvoice.setAppointment(appointment);
        savedInvoice.setServices(Collections.singletonList(petService));
        savedInvoice.setTotalAmount(50.0);
        savedInvoice.setPaymentStatus(PaymentStatus.UNPAID);
        savedInvoice.setCreatedAt(LocalDateTime.now());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        InvoiceResponse response = invoiceService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(50.0, response.getTotalAmount());
        assertEquals(PaymentStatus.UNPAID, response.getPaymentStatus());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void getById_shouldReturnResponse_whenFound() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        InvoiceResponse response = invoiceService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void listAll_shouldReturnListOfResponses() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        when(invoiceRepository.findAll()).thenReturn(Collections.singletonList(invoice));

        List<InvoiceResponse> responses = invoiceService.listAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void updatePaymentStatus_shouldUpdateAndReturnResponse() {
        InvoiceRequest request = new InvoiceRequest();
        request.setPaymentStatus(PaymentStatus.PAID);

        Invoice existingInvoice = new Invoice();
        existingInvoice.setId(1L);
        existingInvoice.setPaymentStatus(PaymentStatus.UNPAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(existingInvoice));

        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setId(1L);
        updatedInvoice.setPaymentStatus(PaymentStatus.PAID);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);

        InvoiceResponse response = invoiceService.updatePaymentStatus(1L, request);

        assertNotNull(response);
        assertEquals(PaymentStatus.PAID, response.getPaymentStatus());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }
}
