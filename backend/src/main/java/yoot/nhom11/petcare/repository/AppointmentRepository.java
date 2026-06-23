package yoot.nhom11.petcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findAllByOwnerIdOrderByAppointmentAtDesc(Long ownerId);
}
