package yoot.nhom11.petcare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.mapper.MedicalRecordMapper;
import yoot.nhom11.petcare.repository.MedicalRecordRepository;
import yoot.nhom11.petcare.service.MedicalRecordService;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordDetailResponse getMedicalRecordDetail(Integer id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findDetailById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical record not found"));
        return medicalRecordMapper.toMedicalRecordDetailResponse(medicalRecord);
    }
}
