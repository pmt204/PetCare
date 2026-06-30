package yoot.nhom11.petcare.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AppointmentBookingRequest(
		@NotNull @Min(1) Long ownerId,
		@NotNull @Min(1) Long petId,
		@NotNull @Min(1) Long veterinarianId,
		@NotNull @Future LocalDateTime appointmentAt,
		@Size(max = 1000) String reasonForVisit
) {
}
