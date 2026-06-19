package yoot.nhom11.petcare.mapper;

import org.springframework.stereotype.Component;
import yoot.nhom11.petcare.dto.response.*;
import yoot.nhom11.petcare.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalRecordMapper {

    public MedicalRecordDetailResponse toMedicalRecordDetailResponse(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        ExaminationResponse examination = ExaminationResponse.builder()
                .medicalRecordId(medicalRecord.getMedical_record_id())
                .date(medicalRecord.getDate())
                .diagnosis(medicalRecord.getDiagnosis())
                .treatment(medicalRecord.getTreatment())
                .build();

        List<PrescriptionResponse> prescriptions = medicalRecord.getPrescriptions() == null ? Collections.emptyList() :
                medicalRecord.getPrescriptions().stream()
                        .map(this::toPrescriptionResponse)
                        .collect(Collectors.toList());

        List<TestResultResponse> testResults = medicalRecord.getTestResults() == null ? Collections.emptyList() :
                medicalRecord.getTestResults().stream()
                        .map(this::toTestResultResponse)
                        .collect(Collectors.toList());

        BillResponse bill = toBillResponse(medicalRecord.getBill());

        return MedicalRecordDetailResponse.builder()
                .examination(examination)
                .prescriptions(prescriptions)
                .testResults(testResults)
                .bill(bill)
                .build();
    }

    public PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionResponse.PrescriptionResponseBuilder builder = PrescriptionResponse.builder()
                .prescriptionId(prescription.getPrescription_id())
                .quantity(prescription.getQuantity());

        if (prescription.getMedicine() != null) {
            builder.medicineId(prescription.getMedicine().getMedicine_id())
                   .medicineName(prescription.getMedicine().getMedicine_name())
                   .unit(prescription.getMedicine().getUnit())
                   .description(prescription.getMedicine().getDescription());
        }

        return builder.build();
    }

    public TestResultResponse toTestResultResponse(TestResult testResult) {
        if (testResult == null) {
            return null;
        }

        return TestResultResponse.builder()
                .testResultId(testResult.getTest_result_id())
                .testName(testResult.getTest_name())
                .result(testResult.getResult())
                .pdfUrl(testResult.getPdf_url())
                .build();
    }

    public BillResponse toBillResponse(Bill bill) {
        if (bill == null) {
            return null;
        }

        return BillResponse.builder()
                .billId(bill.getBill_id())
                .totalPrice(bill.getTotal_price())
                .status(bill.getStatus())
                .build();
    }
}
