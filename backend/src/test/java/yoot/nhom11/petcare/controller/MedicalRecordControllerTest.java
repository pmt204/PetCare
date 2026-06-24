package yoot.nhom11.petcare.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yoot.nhom11.petcare.exception.BusinessException;
import yoot.nhom11.petcare.exception.ErrorCode;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.dto.response.*;
import yoot.nhom11.petcare.service.MedicalRecordService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    private MedicalRecordDetailResponse responseDto;
    private MedicalRecordListResponse listResponseDto;

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

        listResponseDto = MedicalRecordListResponse.builder()
                .medicalRecordId(1)
                .date(new Date())
                .diagnosis("Fever")
                .treatment("Rest")
                .petId(5)
                .petName("Buddy")
                .billId(1)
                .totalPrice(150.0)
                .billStatus("PAID")
                .build();
    }

    @Test
    void getAllMedicalRecords_success() throws Exception {
        when(medicalRecordService.getAllMedicalRecords(any(MedicalRecordFilterRequest.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(listResponseDto)));

        mockMvc.perform(get("/api/medical-records")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "date,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].medicalRecordId").value(1))
                .andExpect(jsonPath("$.content[0].diagnosis").value("Fever"))
                .andExpect(jsonPath("$.content[0].petName").value("Buddy"))
                .andExpect(jsonPath("$.content[0].billStatus").value("PAID"));

        verify(medicalRecordService, times(1)).getAllMedicalRecords(any(MedicalRecordFilterRequest.class), any(Pageable.class));
    }

    @Test
    void getAllMedicalRecords_invalidSortField_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/medical-records")
                        .param("sort", "prescriptions,asc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("SORT_FIELD_INVALID"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/medical-records"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.details.invalidField").value("prescriptions"))
                .andExpect(jsonPath("$.details.allowedFields").exists());

        verify(medicalRecordService, never()).getAllMedicalRecords(any(), any());
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
                .thenThrow(new BusinessException(ErrorCode.MEDICAL_RECORD_NOT_FOUND));

        mockMvc.perform(get("/api/medical-records/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MEDICAL_RECORD_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Medical record not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/medical-records/99"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(medicalRecordService, times(1)).getMedicalRecordDetail(99);
    }
}
