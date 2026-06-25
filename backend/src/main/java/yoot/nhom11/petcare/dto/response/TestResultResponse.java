package yoot.nhom11.petcare.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultResponse {
    private Integer testResultId;
    private String testName;
    private String result;
    private String pdfUrl;
}
