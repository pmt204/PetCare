package yoot.nhom11.petcare.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import yoot.nhom11.petcare.dto.request.MedicalRecordTimelineFilterRequest;
import yoot.nhom11.petcare.dto.response.MedicalRecordDetailResponse;
import yoot.nhom11.petcare.dto.response.MedicalRecordTimelineItemResponse;
import yoot.nhom11.petcare.dto.response.PageResponse;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.entity.MedicalRecordStatus;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.mapper.MedicalRecordMapper;
import yoot.nhom11.petcare.repository.MedicalRecordRepository;
import yoot.nhom11.petcare.repository.PetRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordServiceImpl {

	private final MedicalRecordRepository medicalRecordRepository;
	private final PetRepository petRepository;

	public PageResponse<MedicalRecordTimelineItemResponse> getPetTimeline(Long petId, MedicalRecordTimelineFilterRequest request) {
		Pet pet = petRepository.findById(petId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

		String sortProperty = resolveSortProperty(request.getSortBy());
		Sort.Direction sortDirection = request.getSortDirection() == Sort.Direction.ASC
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortProperty);
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
		Specification<MedicalRecord> specification = Specification.where(hasPetId(pet.getId()));
		specification = andIfPresent(specification, hasStatus(request.getStatus()));
		specification = andIfPresent(specification, visitAtFrom(request.getFromDate()));
		specification = andIfPresent(specification, visitAtTo(request.getToDate()));
		specification = andIfPresent(specification, keywordContains(request.getKeyword()));

		Page<MedicalRecord> page = medicalRecordRepository.findAll(specification, pageable);
		Page<MedicalRecordTimelineItemResponse> mappedPage = page.map(MedicalRecordMapper::toTimelineItem);
		return PageResponse.of(mappedPage, sortProperty, sortDirection.name());
	}

	public MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId) {
		MedicalRecord record = medicalRecordRepository.findById(recordId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical record not found"));

		return MedicalRecordMapper.toDetailResponse(record);
	}

	private String resolveSortProperty(String sortBy) {
		if (sortBy == null || sortBy.isBlank()) {
			return "visitAt";
		}

		String normalized = sortBy.trim();
		return switch (normalized) {
			case "visitAt", "createdAt", "status" -> normalized;
			default -> "visitAt";
		};
	}

	private Specification<MedicalRecord> hasPetId(Long petId) {
		return (root, query, builder) -> builder.equal(root.get("pet").get("id"), petId);
	}

	private Specification<MedicalRecord> hasStatus(MedicalRecordStatus status) {
		if (status == null) {
			return null;
		}

		return (root, query, builder) -> builder.equal(root.get("status"), status);
	}

	private Specification<MedicalRecord> visitAtFrom(LocalDate fromDate) {
		if (fromDate == null) {
			return null;
		}

		Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
		return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("visitAt"), fromInstant);
	}

	private Specification<MedicalRecord> visitAtTo(LocalDate toDate) {
		if (toDate == null) {
			return null;
		}

		Instant toInstant = toDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusNanos(1);
		return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("visitAt"), toInstant);
	}

	private Specification<MedicalRecord> keywordContains(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return null;
		}

		String like = "%" + keyword.trim().toLowerCase() + "%";
		return (root, query, builder) -> builder.or(
				builder.like(builder.lower(root.get("diagnosis")), like),
				builder.like(builder.lower(root.get("reasonForVisit")), like),
				builder.like(builder.lower(root.get("treatmentNote")), like)
		);
	}

	private Specification<MedicalRecord> andIfPresent(Specification<MedicalRecord> base, Specification<MedicalRecord> extra) {
		return extra == null ? base : base.and(extra);
	}
}
