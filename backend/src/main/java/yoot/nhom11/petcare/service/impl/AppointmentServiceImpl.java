package yoot.nhom11.petcare.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import yoot.nhom11.petcare.dto.request.AppointmentBookingRequest;
import yoot.nhom11.petcare.dto.response.AppointmentOptionResponse;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.AppointmentStatus;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.UserRole;
import yoot.nhom11.petcare.repository.AppUserRepository;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.PetRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl {

	private final AppointmentRepository appointmentRepository;
	private final PetRepository petRepository;
	private final AppUserRepository appUserRepository;

	public AppointmentOptionResponse getBookingOptions(Long ownerId) {
		List<AppointmentOptionResponse.PetOption> pets = petRepository.findAllByOwnerIdOrderByNameAsc(ownerId).stream()
				.map(pet -> new AppointmentOptionResponse.PetOption(pet.getId(), pet.getName()))
				.toList();

		List<AppointmentOptionResponse.VeterinarianOption> veterinarians = appUserRepository.findAllByRoleAndActiveTrue(UserRole.VET).stream()
				.map(vet -> new AppointmentOptionResponse.VeterinarianOption(vet.getId(), vet.getFullName()))
				.toList();

		return new AppointmentOptionResponse(pets, veterinarians);
	}

	@Transactional
	public AppointmentResponse createAppointment(AppointmentBookingRequest request) {
		AppUser owner = appUserRepository.findById(request.ownerId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
		Pet pet = petRepository.findByIdAndOwnerId(request.petId(), owner.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
		AppUser veterinarian = appUserRepository.findById(request.veterinarianId())
				.filter(user -> user.getRole() == UserRole.VET && user.isActive())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinarian not found"));

		Appointment appointment = Appointment.builder()
				.owner(owner)
				.pet(pet)
				.veterinarian(veterinarian)
				.appointmentAt(request.appointmentAt())
				.reasonForVisit(request.reasonForVisit())
				.status(AppointmentStatus.REQUESTED)
				.build();

		Appointment saved = appointmentRepository.save(appointment);
		return toResponse(saved);
	}

	public List<AppointmentResponse> getAppointmentsByOwner(Long ownerId) {
		return appointmentRepository.findAllByOwnerIdOrderByAppointmentAtDesc(ownerId).stream()
				.map(this::toResponse)
				.toList();
	}

	private AppointmentResponse toResponse(Appointment appointment) {
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
