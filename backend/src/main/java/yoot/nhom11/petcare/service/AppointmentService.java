package yoot.nhom11.petcare.service;

import java.time.LocalDate;
import java.util.List;

import yoot.nhom11.petcare.dto.request.AppointmentRequest;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;

public interface AppointmentService {
    List<AppointmentResponse> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);
    AppointmentResponse create(AppointmentRequest request);
    AppointmentResponse getById(Long id);
    List<AppointmentResponse> listAll();
    AppointmentResponse update(Long id, AppointmentRequest request);
    void delete(Long id);
}
