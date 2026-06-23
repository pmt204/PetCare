package yoot.nhom11.petcare.service.impl;

import org.springframework.stereotype.Service;
import yoot.nhom11.petcare.dto.request.AppointmentRequest;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.mapper.AppointmentMapper;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<AppointmentResponse> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NoSuchElementException("Doctor not found: " + doctorId);
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + request.getDoctorId()));
        Appointment appointment = AppointmentMapper.toEntity(request, doctor);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse getById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
    }

    @Override
    public List<AppointmentResponse> listAll() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
        AppointmentMapper.updateEntityFromRequest(request, existing);
        Appointment saved = appointmentRepository.save(existing);
        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) throw new NoSuchElementException("Appointment not found: " + id);
        appointmentRepository.deleteById(id);
    }
}