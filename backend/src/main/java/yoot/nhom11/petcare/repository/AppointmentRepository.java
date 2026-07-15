package yoot.nhom11.petcare.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import yoot.nhom11.petcare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
}
