package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
