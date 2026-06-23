package yoot.nhom11.petcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoot.nhom11.petcare.dto.request.TestResultRequest;
import yoot.nhom11.petcare.dto.response.TestResultResponse;
import yoot.nhom11.petcare.service.TestResultService;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    private final TestResultService service;

    public TestResultController(TestResultService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TestResultResponse> create(@RequestBody TestResultRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}