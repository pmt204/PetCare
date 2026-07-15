package yoot.nhom11.petcare.mapper;

import java.util.List;

import yoot.nhom11.petcare.dto.request.AppointmentBookingRequest;
import yoot.nhom11.petcare.dto.request.AppointmentListFilterRequest;
import yoot.nhom11.petcare.dto.request.AppointmentRequest;
import yoot.nhom11.petcare.dto.response.AppointmentOptionResponse;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.Pet;

public final class AppointmentMapper {

	private AppointmentMapper() {
	}

	public static AppointmentOptionResponse toOptionResponse(List<Pet> pets, List<AppUser> veterinarians) {
		return new AppointmentOptionResponse(toPetOptions(pets), toVeterinarianOptions(veterinarians));
	}

	public static List<AppointmentOptionResponse.PetOption> toPetOptions(List<Pet> pets) {
		return pets.stream()
				.map(pet -> new AppointmentOptionResponse.PetOption(
						pet.getId(), 
						pet.getName(),
						pet.getSpecies() != null ? pet.getSpecies().name() : "OTHER",
						pet.getBreed()
				))
				.toList();
	}

	public static List<AppointmentOptionResponse.VeterinarianOption> toVeterinarianOptions(List<AppUser> veterinarians) {
		return veterinarians.stream()
				.map(vet -> new AppointmentOptionResponse.VeterinarianOption(vet.getId(), vet.getFullName()))
				.toList();
	}

	public static AppointmentResponse toAppointmentResponse(Appointment appointment) {
		AppointmentResponse response = new AppointmentResponse(
				appointment.getId(),
				new AppointmentResponse.OwnerSummary(
						appointment.getOwner().getId(),
						appointment.getOwner().getFullName(),
						appointment.getOwner().getEmail(),
						appointment.getOwner().getPhone()
				),
				new AppointmentResponse.PetSummary(
						appointment.getPet().getId(),
						appointment.getPet().getName(),
						appointment.getPet().getSpecies(),
						appointment.getPet().getBreed()
				),
				new AppointmentResponse.VeterinarianSummary(
						appointment.getVeterinarian().getId(),
						appointment.getVeterinarian().getFullName(),
						appointment.getVeterinarian().getEmail()
				),
				appointment.getAppointmentAt(),
				appointment.getReasonForVisit(),
				appointment.getStatus()
		);
		response.setPaymentMethod(appointment.getPaymentMethod());
		response.setPaymentStatus(appointment.getPaymentStatus());
		return response;
	}

    // Methods for tai/admin's branch compatibility:
    public static Appointment toEntity(AppointmentRequest r, Doctor doctor) {
        Appointment a = new Appointment();
        a.setDoctor(doctor);
        a.setPatientName(r.getPatientName());
        a.setPatientPhone(r.getPatientPhone());
        a.setAppointmentTime(r.getAppointmentTime());
        a.setAppointmentAt(r.getAppointmentTime()); // Sync both fields
        a.setReason(r.getReason());
        return a;
    }

    public static AppointmentResponse toResponse(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setDoctorId(a.getDoctor() != null ? a.getDoctor().getId() : null);
        r.setDoctorName(a.getDoctor() != null ? a.getDoctor().getName() : null);
        r.setPatientName(a.getPatientName());
        r.setPatientPhone(a.getPatientPhone());
        r.setAppointmentTime(a.getAppointmentTime());
        r.setReason(a.getReason());
        r.setStatus(a.getStatusStr());
        r.setPaymentMethod(a.getPaymentMethod());
        r.setPaymentStatus(a.getPaymentStatus());
        
        if (a.getOwner() != null) {
            r.setOwner(new AppointmentResponse.OwnerSummary(
                a.getOwner().getId(),
                a.getOwner().getFullName(),
                a.getOwner().getEmail(),
                a.getOwner().getPhone()
            ));
        }
        if (a.getPet() != null) {
            r.setPet(new AppointmentResponse.PetSummary(
                a.getPet().getId(),
                a.getPet().getName(),
                a.getPet().getSpecies(),
                a.getPet().getBreed()
            ));
        }
        if (a.getVeterinarian() != null) {
            r.setVeterinarian(new AppointmentResponse.VeterinarianSummary(
                a.getVeterinarian().getId(),
                a.getVeterinarian().getFullName(),
                a.getVeterinarian().getEmail()
            ));
        }
        return r;
    }

    public static void updateEntityFromRequest(AppointmentRequest req, Appointment a) {
        if (req.getPatientName() != null) a.setPatientName(req.getPatientName());
        if (req.getPatientPhone() != null) a.setPatientPhone(req.getPatientPhone());
        if (req.getAppointmentTime() != null) {
            a.setAppointmentTime(req.getAppointmentTime());
            a.setAppointmentAt(req.getAppointmentTime()); // Sync both fields
        }
        if (req.getReason() != null) a.setReason(req.getReason());
    }
}
