package yoot.nhom11.petcare.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import yoot.nhom11.petcare.dto.request.AppointmentBookingRequest;
import yoot.nhom11.petcare.dto.request.AppointmentListFilterRequest;
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

	public List<AppointmentResponse> getAppointmentsByOwner(Long ownerId, AppointmentListFilterRequest request) {
		String sortProperty = resolveSortProperty(request.getSortBy());
		Sort.Direction sortDirection = request.getSortDirection() == Sort.Direction.ASC
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(sortDirection, sortProperty));

		Specification<Appointment> specification = Specification.where(hasOwnerId(ownerId));
		specification = andIfPresent(specification, hasPetId(request.getPetId()));
		specification = andIfPresent(specification, hasVeterinarianId(request.getVeterinarianId()));
		specification = andIfPresent(specification, hasStatus(request.getStatus()));
		specification = andIfPresent(specification, appointmentAtFrom(request.getFromDate()));
		specification = andIfPresent(specification, appointmentAtTo(request.getToDate()));
		specification = andIfPresent(specification, keywordContains(request.getKeyword()));

		return appointmentRepository.findAll(specification, pageable).stream()
				.map(this::toResponse)
				.toList();
	}

	private String resolveSortProperty(String sortBy) {
		if (sortBy == null || sortBy.isBlank()) {
			return "appointmentAt";
		}

		String normalized = sortBy.trim();
		return switch (normalized) {
			case "appointmentAt", "createdAt", "status" -> normalized;
			default -> "appointmentAt";
		};
	}

	private Specification<Appointment> hasOwnerId(Long ownerId) {
		return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
	}

	private Specification<Appointment> hasPetId(Long petId) {
		if (petId == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("pet").get("id"), petId);
	}

	private Specification<Appointment> hasVeterinarianId(Long veterinarianId) {
		if (veterinarianId == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("veterinarian").get("id"), veterinarianId);
	}

	private Specification<Appointment> hasStatus(AppointmentStatus status) {
		if (status == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("status"), status);
	}

	private Specification<Appointment> appointmentAtFrom(LocalDate fromDate) {
		if (fromDate == null) {
			return null;
		}

		LocalDateTime fromDateTime = fromDate.atStartOfDay();
		return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("appointmentAt"), fromDateTime);
	}

	private Specification<Appointment> appointmentAtTo(LocalDate toDate) {
		if (toDate == null) {
			return null;
		}

		LocalDateTime toDateTime = toDate.plusDays(1).atStartOfDay().minusNanos(1);
		return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("appointmentAt"), toDateTime);
	}

	private Specification<Appointment> keywordContains(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return null;
		}

		String like = "%" + keyword.trim().toLowerCase() + "%";
		return (root, query, builder) -> builder.like(builder.lower(root.get("reasonForVisit")), like);
	}

	private Specification<Appointment> andIfPresent(Specification<Appointment> base, Specification<Appointment> extra) {
		return extra == null ? base : base.and(extra);
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
