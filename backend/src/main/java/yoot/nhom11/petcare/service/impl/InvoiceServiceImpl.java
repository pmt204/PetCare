package yoot.nhom11.petcare.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoot.nhom11.petcare.dto.request.InvoiceRequest;
import yoot.nhom11.petcare.dto.response.InvoiceResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Invoice;
import yoot.nhom11.petcare.entity.PetService;
import yoot.nhom11.petcare.mapper.InvoiceMapper;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.InvoiceRepository;
import yoot.nhom11.petcare.repository.PetServiceRepository;
import yoot.nhom11.petcare.service.InvoiceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PetServiceRepository petServiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, AppointmentRepository appointmentRepository, PetServiceRepository petServiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.petServiceRepository = petServiceRepository;
    }

    @Override
    @Transactional
    public InvoiceResponse create(InvoiceRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + request.getAppointmentId()));

        List<PetService> services = petServiceRepository.findAllById(request.getServiceIds());
        if (services.size() != request.getServiceIds().size()) {
            throw new NoSuchElementException("One or more services not found.");
        }

        double totalAmount = services.stream().mapToDouble(PetService::getPrice).sum();

        Invoice invoice = new Invoice();
        invoice.setAppointment(appointment);
        invoice.setServices(services);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaymentStatus(request.getPaymentStatus());
        invoice.setCreatedAt(LocalDateTime.now());

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceMapper.toResponse(saved);
    }

    @Override
    public InvoiceResponse getById(Long id) {
        return invoiceRepository.findById(id)
                .map(InvoiceMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Invoice not found: " + id));
    }

    @Override
    public List<InvoiceResponse> listAll() {
        // Use eager fetching to avoid LazyInitializationException
        return invoiceRepository.findAllWithDetails().stream()
                .map(InvoiceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceResponse updatePaymentStatus(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invoice not found: " + id));
        invoice.setPaymentStatus(request.getPaymentStatus());
        
        if (invoice.getAppointment() != null && request.getPaymentStatus() != null) {
            invoice.getAppointment().setPaymentStatus(request.getPaymentStatus().name());
            appointmentRepository.save(invoice.getAppointment());
        }

        Invoice updated = invoiceRepository.save(invoice);
        return InvoiceMapper.toResponse(updated);
    }
}
