package yoot.nhom11.petcare.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.response.*;
import yoot.nhom11.petcare.service.MedicalRecordService;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicalRecordControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    private MedicalRecordDetailResponse responseDto;

    @BeforeEach
    void setUp() {
        ExaminationResponse exam = ExaminationResponse.builder()
                .medicalRecordId(1)
                .date(new Date())
                .diagnosis("Fever")
                .treatment("Rest")
                .build();

        PrescriptionResponse prescription = PrescriptionResponse.builder()
                .prescriptionId(1)
                .quantity(5)
                .medicineId(10)
                .medicineName("Amoxicillin")
                .unit("Tablet")
                .description("Antibiotic")
                .build();

        TestResultResponse testResult = TestResultResponse.builder()
                .testResultId(1)
                .testName("Blood Test")
                .result("Normal")
                .pdfUrl("http://url/pdf")
                .build();

        BillResponse bill = BillResponse.builder()
                .billId(1)
                .totalPrice(150.0)
                .status("PAID")
                .build();

        responseDto = MedicalRecordDetailResponse.builder()
                .examination(exam)
                .prescriptions(Collections.singletonList(prescription))
                .testResults(Collections.singletonList(testResult))
                .bill(bill)
                .build();
    }

    @Test
    void getMedicalRecordDetail_success() throws Exception {
        when(medicalRecordService.getMedicalRecordDetail(1)).thenReturn(responseDto);

        mockMvc.perform(get("/api/medical-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.examination.medicalRecordId").value(1))
                .andExpect(jsonPath("$.examination.diagnosis").value("Fever"))
                .andExpect(jsonPath("$.prescriptions.size()").value(1))
                .andExpect(jsonPath("$.prescriptions[0].medicineName").value("Amoxicillin"))
                .andExpect(jsonPath("$.testResults.size()").value(1))
                .andExpect(jsonPath("$.testResults[0].testName").value("Blood Test"))
                .andExpect(jsonPath("$.bill.billId").value(1))
                .andExpect(jsonPath("$.bill.status").value("PAID"));

        verify(medicalRecordService, times(1)).getMedicalRecordDetail(1);
    }

    @Test
    void getMedicalRecordDetail_notFound() throws Exception {
        when(medicalRecordService.getMedicalRecordDetail(99))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical record not found"));

        mockMvc.perform(get("/api/medical-records/99"))
                .andExpect(status().isNotFound());

        verify(medicalRecordService, times(1)).getMedicalRecordDetail(99);
    }
}
