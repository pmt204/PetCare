package yoot.nhom11.petcare.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
		List<T> content,
		int page,
		int size,
		long totalElements,
		int totalPages,
		String sortBy,
		String sortDirection
) {
	public static <T> PageResponse<T> of(Page<T> page, String sortBy, String sortDirection) {
		return new PageResponse<>(
				page.getContent(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages(),
				sortBy,
				sortDirection
		);
	}
}
