package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
