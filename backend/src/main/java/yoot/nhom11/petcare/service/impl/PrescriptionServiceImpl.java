package yoot.nhom11.petcare.service.impl;

import org.springframework.stereotype.Service;
import yoot.nhom11.petcare.dto.request.PrescriptionRequest;
import yoot.nhom11.petcare.dto.response.PrescriptionResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.Prescription;
import yoot.nhom11.petcare.mapper.PrescriptionMapper;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.repository.PrescriptionRepository;
import yoot.nhom11.petcare.service.PrescriptionService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public PrescriptionResponse create(PrescriptionRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + request.getDoctorId()));
        Prescription prescription = PrescriptionMapper.toEntity(request, doctor);
        Prescription saved = prescriptionRepository.save(prescription);
        return PrescriptionMapper.toResponse(saved);
    }

    @Override
    public PrescriptionResponse getById(Long id) {
        return prescriptionRepository.findById(id)
                .map(PrescriptionMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Prescription not found: " + id));
    }

    @Override
    public List<PrescriptionResponse> listAll() {
        return prescriptionRepository.findAll().stream()
                .map(PrescriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponse update(Long id, PrescriptionRequest request) {
        throw new UnsupportedOperationException("Update operation is not supported.");
    }

    @Override
    public void delete(Long id) {
        if (!prescriptionRepository.existsById(id)) throw new NoSuchElementException("Prescription not found: " + id);
        prescriptionRepository.deleteById(id);
    }
}