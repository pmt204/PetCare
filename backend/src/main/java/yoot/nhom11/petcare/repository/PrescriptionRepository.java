package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import yoot.nhom11.petcare.entity.Prescription;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    @Query("SELECT p FROM Prescription p LEFT JOIN FETCH p.doctor LEFT JOIN FETCH p.medicalRecord mr LEFT JOIN FETCH mr.pet")
    List<Prescription> findAllWithDetails();
}
