package yoot.nhom11.petcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import yoot.nhom11.petcare.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long>, JpaSpecificationExecutor<MedicalRecord> {

    @Query("SELECT mr FROM MedicalRecord mr " +
           "LEFT JOIN FETCH mr.invoice " +
           "LEFT JOIN FETCH mr.prescriptions p " +
           "LEFT JOIN FETCH p.medicine " +
           "WHERE mr.id = :id")
    Optional<MedicalRecord> findDetailById(@Param("id") Long id);

    default Optional<MedicalRecord> findDetailById(Integer id) {
        return id != null ? findDetailById(id.longValue()) : Optional.empty();
    }
}
