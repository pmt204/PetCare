package yoot.nhom11.petcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import yoot.nhom11.petcare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

	List<Appointment> findAllByOwnerIdOrderByAppointmentAtDesc(Long ownerId);
}
