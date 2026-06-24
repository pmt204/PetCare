package yoot.nhom11.petcare.dto.request;

public class TestResultRequest {
    private Long doctorId;
    private String patientName;
    private String testType;
    private String filePath;
    private String result;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
