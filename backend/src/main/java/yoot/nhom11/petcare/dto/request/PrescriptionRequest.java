package yoot.nhom11.petcare.dto.request;

public class PrescriptionRequest {
    private Long doctorId;
    private String patientName;
    private String medicineList;
    private String instructions;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getMedicineList() { return medicineList; }
    public void setMedicineList(String medicineList) { this.medicineList = medicineList; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
