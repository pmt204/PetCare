package yoot.nhom11.petcare.service;

import yoot.nhom11.petcare.dto.request.TestResultRequest;
import yoot.nhom11.petcare.dto.response.TestResultResponse;

import java.util.List;

public interface TestResultService {
    TestResultResponse create(TestResultRequest request);
    TestResultResponse getById(Long id);
    List<TestResultResponse> listAll();
    TestResultResponse update(Long id, TestResultRequest request);
    void delete(Long id);
}
