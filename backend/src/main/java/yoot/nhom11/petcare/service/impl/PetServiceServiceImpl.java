package yoot.nhom11.petcare.service.impl;

import org.springframework.stereotype.Service;
import yoot.nhom11.petcare.dto.request.PetServiceRequest;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;
import yoot.nhom11.petcare.entity.PetService;
import yoot.nhom11.petcare.mapper.PetServiceMapper;
import yoot.nhom11.petcare.repository.PetServiceRepository;
import yoot.nhom11.petcare.service.PetServiceService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PetServiceServiceImpl implements PetServiceService {

    private final PetServiceRepository petServiceRepository;

    public PetServiceServiceImpl(PetServiceRepository petServiceRepository) {
        this.petServiceRepository = petServiceRepository;
    }

    @Override
    public PetServiceResponse create(PetServiceRequest request) {
        PetService petService = PetServiceMapper.toEntity(request);
        PetService saved = petServiceRepository.save(petService);
        return PetServiceMapper.toResponse(saved);
    }

    @Override
    public PetServiceResponse getById(Long id) {
        return petServiceRepository.findById(id)
                .map(PetServiceMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("PetService not found: " + id));
    }

    @Override
    public List<PetServiceResponse> listAll() {
        return petServiceRepository.findAll().stream()
                .map(PetServiceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PetServiceResponse update(Long id, PetServiceRequest request) {
        PetService existingService = petServiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("PetService not found: " + id));
        existingService.setName(request.getName());
        existingService.setDescription(request.getDescription());
        existingService.setPrice(request.getPrice());
        PetService updated = petServiceRepository.save(existingService);
        return PetServiceMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!petServiceRepository.existsById(id)) {
            throw new NoSuchElementException("PetService not found: " + id);
        }
        petServiceRepository.deleteById(id);
    }
}
