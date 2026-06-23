package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
