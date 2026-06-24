package yoot.nhom11.petcare.mapper;

import org.junit.jupiter.api.Test;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.entity.*;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordMapperTest {

    private final MedicalRecordMapper mapper = new MedicalRecordMapper();

    @Test
    void toMedicalRecordDetailResponse_null_returnsNull() {
        assertNull(mapper.toMedicalRecordDetailResponse(null));
    }

    @Test
    void toMedicalRecordDetailResponse_fullRecord_mapsSuccessfully() {
        Date examDate = new Date();
        Medicine medicine = Medicine.builder()
                .medicineId(10)
                .medicineName("Amoxicillin")
                .unit("Tablet")
                .description("Antibiotic")
                .build();

        Prescription prescription1 = Prescription.builder()
                .prescriptionId(1)
                .quantity(5)
                .medicine(medicine)
                .build();

        TestResult testResult1 = TestResult.builder()
                .testResultId(1)
                .testName("Blood Test")
                .result("Normal")
                .pdfUrl("http://url/pdf")
                .build();

        Bill bill = Bill.builder()
                .billId(1)
                .totalPrice(150.0)
                .status("PAID")
                .build();

        MedicalRecord record = MedicalRecord.builder()
                .medicalRecordId(1)
                .date(examDate)
                .diagnosis("Fever")
                .treatment("Rest and pills")
                .prescriptions(Arrays.asList(prescription1))
                .testResults(Arrays.asList(testResult1))
                .bill(bill)
                .build();

        MedicalRecordDetailResponse response = mapper.toMedicalRecordDetailResponse(record);

        assertNotNull(response);
        assertNotNull(response.getExamination());
        assertEquals(1, response.getExamination().getMedicalRecordId());
        assertEquals(examDate, response.getExamination().getDate());
        assertEquals("Fever", response.getExamination().getDiagnosis());
        assertEquals("Rest and pills", response.getExamination().getTreatment());

        assertNotNull(response.getPrescriptions());
        assertEquals(1, response.getPrescriptions().size());
        assertEquals(1, response.getPrescriptions().get(0).getPrescriptionId());
        assertEquals(5, response.getPrescriptions().get(0).getQuantity());
        assertEquals(10, response.getPrescriptions().get(0).getMedicineId());
        assertEquals("Amoxicillin", response.getPrescriptions().get(0).getMedicineName());
        assertEquals("Tablet", response.getPrescriptions().get(0).getUnit());
        assertEquals("Antibiotic", response.getPrescriptions().get(0).getDescription());

        assertNotNull(response.getTestResults());
        assertEquals(1, response.getTestResults().size());
        assertEquals(1, response.getTestResults().get(0).getTestResultId());
        assertEquals("Blood Test", response.getTestResults().get(0).getTestName());
        assertEquals("Normal", response.getTestResults().get(0).getResult());
        assertEquals("http://url/pdf", response.getTestResults().get(0).getPdfUrl());

        assertNotNull(response.getBill());
        assertEquals(1, response.getBill().getBillId());
        assertEquals(150.0, response.getBill().getTotalPrice());
        assertEquals("PAID", response.getBill().getStatus());
    }

    @Test
    void toMedicalRecordDetailResponse_nullCollections_mapsToEmptyLists() {
        MedicalRecord record = MedicalRecord.builder()
                .medicalRecordId(1)
                .date(new Date())
                .diagnosis("Fever")
                .treatment("Rest")
                .prescriptions(null)
                .testResults(null)
                .bill(null)
                .build();

        MedicalRecordDetailResponse response = mapper.toMedicalRecordDetailResponse(record);

        assertNotNull(response);
        assertNotNull(response.getPrescriptions());
        assertTrue(response.getPrescriptions().isEmpty());
        assertNotNull(response.getTestResults());
        assertTrue(response.getTestResults().isEmpty());
        assertNull(response.getBill());
    }
}
