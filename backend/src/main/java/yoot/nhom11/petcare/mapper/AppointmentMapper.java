package yoot.nhom11.petcare.mapper;

import yoot.nhom11.petcare.dto.request.AppointmentRequest;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Doctor;

public class AppointmentMapper {

    public static Appointment toEntity(AppointmentRequest r, Doctor doctor) {
        Appointment a = new Appointment();
        a.setDoctor(doctor);
        a.setPatientName(r.getPatientName());
        a.setPatientPhone(r.getPatientPhone());
        a.setAppointmentTime(r.getAppointmentTime());
        a.setReason(r.getReason());
        return a;
    }

    public static AppointmentResponse toResponse(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setDoctorId(a.getDoctor().getId());
        r.setDoctorName(a.getDoctor().getName());
        r.setPatientName(a.getPatientName());
        r.setPatientPhone(a.getPatientPhone());
        r.setAppointmentTime(a.getAppointmentTime());
        r.setReason(a.getReason());
        r.setStatus(a.getStatus());
        return r;
    }

    public static void updateEntityFromRequest(AppointmentRequest req, Appointment a) {
        if (req.getPatientName() != null) a.setPatientName(req.getPatientName());
        if (req.getPatientPhone() != null) a.setPatientPhone(req.getPatientPhone());
        if (req.getAppointmentTime() != null) a.setAppointmentTime(req.getAppointmentTime());
        if (req.getReason() != null) a.setReason(req.getReason());
    }
}