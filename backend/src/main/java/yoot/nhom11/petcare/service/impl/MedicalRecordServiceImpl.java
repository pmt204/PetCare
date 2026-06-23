package yoot.nhom11.petcare.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import yoot.nhom11.petcare.dto.request.MedicalRecordRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.mapper.MedicalRecordMapper;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.repository.MedicalRecordRepository;
import yoot.nhom11.petcare.service.MedicalRecordService;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, DoctorRepository doctorRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + request.getDoctorId()));
        MedicalRecord medicalRecord = MedicalRecordMapper.toEntity(request, doctor);
        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);
        return MedicalRecordMapper.toResponse(saved);
    }

    @Override
    public MedicalRecordResponse getById(Long id) {
        return medicalRecordRepository.findById(id)
                .map(MedicalRecordMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Medical Record not found: " + id));
    }

    @Override
    public List<MedicalRecordResponse> listAll() {
        return medicalRecordRepository.findAll().stream()
                .map(MedicalRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        // For simplicity, update is not fully implemented as request DTO doesn't cover all fields for update
        // A proper implementation would require a different DTO or logic
        throw new UnsupportedOperationException("Update operation is not supported.");
    }

    @Override
    public void delete(Long id) {
        if (!medicalRecordRepository.existsById(id)) throw new NoSuchElementException("Medical Record not found: " + id);
        medicalRecordRepository.deleteById(id);
    }
}