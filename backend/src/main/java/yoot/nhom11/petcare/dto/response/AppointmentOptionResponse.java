package yoot.nhom11.petcare.dto.response;

import java.util.List;

public record AppointmentOptionResponse(
		List<PetOption> pets,
		List<VeterinarianOption> veterinarians
) {
	public record PetOption(Long id, String name) {
	}

	public record VeterinarianOption(Long id, String fullName) {
	}
}
