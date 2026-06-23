package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yoot.nhom11.petcare.entity.MedicalRecord;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    @Query("SELECT mr FROM MedicalRecord mr " +
           "LEFT JOIN FETCH mr.bill " +
           "LEFT JOIN FETCH mr.prescriptions p " +
           "LEFT JOIN FETCH p.medicine " +
           "WHERE mr.medical_record_id = :id")
    Optional<MedicalRecord> findDetailById(@Param("id") Integer id);
}
