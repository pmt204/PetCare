package yoot.nhom11.petcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import yoot.nhom11.petcare.exception.BusinessException;
import yoot.nhom11.petcare.exception.ErrorCode;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordListResponse;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.mapper.MedicalRecordMapper;
import yoot.nhom11.petcare.mapper.MedicalRecordMapperImpl;
import yoot.nhom11.petcare.repository.MedicalRecordRepository;
import yoot.nhom11.petcare.service.impl.MedicalRecordServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Spy
    private MedicalRecordMapper medicalRecordMapper = new MedicalRecordMapperImpl();

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = MedicalRecord.builder()
                .medicalRecordId(1)
                .diagnosis("Fever")
                .treatment("Rest")
                .build();
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllMedicalRecords_success() {
        MedicalRecordFilterRequest filter = new MedicalRecordFilterRequest();
        Pageable pageable = PageRequest.of(0, 10);
        when(medicalRecordRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Arrays.asList(medicalRecord)));

        Page<MedicalRecordListResponse> responses = medicalRecordService.getAllMedicalRecords(filter, pageable);

        assertNotNull(responses);
        assertEquals(1, responses.getContent().size());
        assertEquals("Fever", responses.getContent().get(0).getDiagnosis());
        verify(medicalRecordRepository, times(1)).findAll(any(Specification.class), eq(pageable));
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

        BusinessException exception = assertThrows(BusinessException.class,
                () -> medicalRecordService.getMedicalRecordDetail(99));

        assertEquals(ErrorCode.MEDICAL_RECORD_NOT_FOUND, exception.getErrorCode());
        assertEquals("Medical record not found", exception.getMessage());
        verify(medicalRecordRepository, times(1)).findDetailById(99);
    }
}
