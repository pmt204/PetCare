package yoot.nhom11.petcare.mapper;

import java.util.List;

import yoot.nhom11.petcare.dto.response.AppointmentOptionResponse;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.Pet;

public final class AppointmentMapper {

	private AppointmentMapper() {
	}

	public static AppointmentOptionResponse toOptionResponse(List<Pet> pets, List<AppUser> veterinarians) {
		return new AppointmentOptionResponse(toPetOptions(pets), toVeterinarianOptions(veterinarians));
	}

	public static List<AppointmentOptionResponse.PetOption> toPetOptions(List<Pet> pets) {
		return pets.stream()
				.map(pet -> new AppointmentOptionResponse.PetOption(pet.getId(), pet.getName()))
				.toList();
	}

	public static List<AppointmentOptionResponse.VeterinarianOption> toVeterinarianOptions(List<AppUser> veterinarians) {
		return veterinarians.stream()
				.map(vet -> new AppointmentOptionResponse.VeterinarianOption(vet.getId(), vet.getFullName()))
				.toList();
	}

	public static AppointmentResponse toAppointmentResponse(Appointment appointment) {
		return new AppointmentResponse(
				appointment.getId(),
				new AppointmentResponse.OwnerSummary(
						appointment.getOwner().getId(),
						appointment.getOwner().getFullName(),
						appointment.getOwner().getEmail()
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
	}
}
