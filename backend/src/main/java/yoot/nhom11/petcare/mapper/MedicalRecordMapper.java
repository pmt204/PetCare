package yoot.nhom11.petcare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import yoot.nhom11.petcare.dto.response.*;
import yoot.nhom11.petcare.entity.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface MedicalRecordMapper {

    @Mapping(source = ".", target = "examination")
    MedicalRecordDetailResponse toMedicalRecordDetailResponse(MedicalRecord medicalRecord);

    ExaminationResponse toExaminationResponse(MedicalRecord medicalRecord);

    @Mapping(source = "medicine.medicineId", target = "medicineId")
    @Mapping(source = "medicine.medicineName", target = "medicineName")
    @Mapping(source = "medicine.unit", target = "unit")
    @Mapping(source = "medicine.description", target = "description")
    PrescriptionResponse toPrescriptionResponse(Prescription prescription);

    TestResultResponse toTestResultResponse(TestResult testResult);

    BillResponse toBillResponse(Bill bill);

    @Mapping(source = "pet.petId", target = "petId")
    @Mapping(source = "pet.petName", target = "petName")
    @Mapping(source = "bill.billId", target = "billId")
    @Mapping(source = "bill.totalPrice", target = "totalPrice")
    @Mapping(source = "bill.status", target = "billStatus")
    MedicalRecordListResponse toMedicalRecordListResponse(MedicalRecord medicalRecord);
}

