package yoot.nhom11.petcare.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.DoctorResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.mapper.DoctorMapper;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;

    public DoctorServiceImpl(DoctorRepository repository) {
        this.repository = repository;
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        Doctor d = DoctorMapper.toEntity(request);
        Doctor saved = repository.save(d);
        return DoctorMapper.toResponse(saved);
    }

    @Override
    public DoctorResponse getById(Long id) {
        return repository.findById(id)
                .map(DoctorMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + id));
    }

    @Override
    public List<DoctorResponse> listAll() {
        return repository.findAll().stream().map(DoctorMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor exist = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Doctor not found: " + id));
        DoctorMapper.updateEntityFromRequest(request, exist);
        Doctor saved = repository.save(exist);
        return DoctorMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NoSuchElementException("Doctor not found: " + id);
        repository.deleteById(id);
    }
}
