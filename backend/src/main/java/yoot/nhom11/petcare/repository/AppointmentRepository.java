package yoot.nhom11.petcare.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    long countByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
}
