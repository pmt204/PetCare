package yoot.nhom11.petcare.dto.response;

import java.time.LocalDateTime;

import yoot.nhom11.petcare.entity.AppointmentStatus;
import yoot.nhom11.petcare.entity.PetSpecies;

public record AppointmentResponse(
		Long id,
		OwnerSummary owner,
		PetSummary pet,
		VeterinarianSummary veterinarian,
		LocalDateTime appointmentAt,
		String reasonForVisit,
		AppointmentStatus status
) {
	public record OwnerSummary(Long id, String fullName, String email) {
	}

	public record PetSummary(Long id, String name, PetSpecies species, String breed) {
	}

	public record VeterinarianSummary(Long id, String fullName, String email) {
	}
}
