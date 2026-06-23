package yoot.nhom11.petcare.service.impl;

import org.springframework.stereotype.Service;
import yoot.nhom11.petcare.dto.request.TestResultRequest;
import yoot.nhom11.petcare.dto.response.TestResultResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.TestResult;
import yoot.nhom11.petcare.mapper.TestResultMapper;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.repository.TestResultRepository;
import yoot.nhom11.petcare.service.TestResultService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository testResultRepository;
    private final DoctorRepository doctorRepository;

    public TestResultServiceImpl(TestResultRepository testResultRepository, DoctorRepository doctorRepository) {
        this.testResultRepository = testResultRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public TestResultResponse create(TestResultRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + request.getDoctorId()));
        TestResult testResult = TestResultMapper.toEntity(request, doctor);
        TestResult saved = testResultRepository.save(testResult);
        return TestResultMapper.toResponse(saved);
    }

    @Override
    public TestResultResponse getById(Long id) {
        return testResultRepository.findById(id)
                .map(TestResultMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Test Result not found: " + id));
    }

    @Override
    public List<TestResultResponse> listAll() {
        return testResultRepository.findAll().stream()
                .map(TestResultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TestResultResponse update(Long id, TestResultRequest request) {
        throw new UnsupportedOperationException("Update operation is not supported.");
    }

    @Override
    public void delete(Long id) {
        if (!testResultRepository.existsById(id)) throw new NoSuchElementException("Test Result not found: " + id);
        testResultRepository.deleteById(id);
    }
}