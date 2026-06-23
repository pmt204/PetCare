package yoot.nhom11.petcare.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import yoot.nhom11.petcare.dto.request.AppointmentBookingRequest;
import yoot.nhom11.petcare.dto.response.AppointmentOptionResponse;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.service.impl.AppointmentServiceImpl;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentServiceImpl appointmentService;

	@GetMapping("/form-data")
	public AppointmentOptionResponse getFormData(@RequestParam Long ownerId) {
		return appointmentService.getBookingOptions(ownerId);
	}

	@PostMapping
	public AppointmentResponse create(@Valid @RequestBody AppointmentBookingRequest request) {
		return appointmentService.createAppointment(request);
	}

	@GetMapping
	public List<AppointmentResponse> listByOwner(@RequestParam Long ownerId) {
		return appointmentService.getAppointmentsByOwner(ownerId);
	}
}
