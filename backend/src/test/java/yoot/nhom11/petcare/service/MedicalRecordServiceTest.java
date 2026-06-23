package yoot.nhom11.petcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.mapper.MedicalRecordMapper;
import yoot.nhom11.petcare.repository.MedicalRecordRepository;
import yoot.nhom11.petcare.service.impl.MedicalRecordServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Spy
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = MedicalRecord.builder()
                .medical_record_id(1)
                .diagnosis("Fever")
                .treatment("Rest")
                .build();
    }

    @Test
    void getMedicalRecordDetail_success() {
        when(medicalRecordRepository.findDetailById(1)).thenReturn(Optional.of(medicalRecord));

        MedicalRecordDetailResponse response = medicalRecordService.getMedicalRecordDetail(1);

        assertNotNull(response);
        assertEquals(1, response.getExamination().getMedicalRecordId());
        assertEquals("Fever", response.getExamination().getDiagnosis());
        verify(medicalRecordRepository, times(1)).findDetailById(1);
    }

    @Test
    void getMedicalRecordDetail_notFound() {
        when(medicalRecordRepository.findDetailById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> medicalRecordService.getMedicalRecordDetail(99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Medical record not found", exception.getReason());
        verify(medicalRecordRepository, times(1)).findDetailById(99);
    }
}
