package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultResponse {
    // hoai's fields
    private Integer testResultId;
    private String testName;
    private String result;
    private String pdfUrl;

    // tai/admin's fields
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private LocalDateTime uploadedDate;
    private String testType;
    private String filePath;
    private String status;
}
