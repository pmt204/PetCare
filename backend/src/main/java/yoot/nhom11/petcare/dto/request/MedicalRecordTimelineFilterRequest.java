package yoot.nhom11.petcare.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Sort;

import yoot.nhom11.petcare.entity.MedicalRecordStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalRecordTimelineFilterRequest {

	@Min(0)
	private int page = 0;

	@Min(1)
	@Max(100)
	private int size = 10;

	private String sortBy = "visitAt";

	private Sort.Direction sortDirection = Sort.Direction.DESC;

	private MedicalRecordStatus status;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fromDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate toDate;

	private String keyword;
}
