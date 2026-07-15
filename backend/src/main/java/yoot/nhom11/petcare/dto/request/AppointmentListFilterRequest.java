package yoot.nhom11.petcare.dto.request;

import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import yoot.nhom11.petcare.entity.AppointmentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentListFilterRequest {

	@Min(0)
	private int page = 0;

	@Min(1)
	@Max(100)
	private int size = 10;

	private String sortBy = "appointmentAt";

	private Sort.Direction sortDirection = Sort.Direction.DESC;

	private Long petId;

	private Long veterinarianId;

	private AppointmentStatus status;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fromDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate toDate;

	private String keyword;
}
