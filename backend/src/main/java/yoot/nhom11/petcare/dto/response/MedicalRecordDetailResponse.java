package yoot.nhom11.petcare.dto.response;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDetailResponse {
    private ExaminationResponse examination;
    private List<PrescriptionResponse> prescriptions;
    private List<TestResultResponse> testResults;
    private BillResponse bill;
}
