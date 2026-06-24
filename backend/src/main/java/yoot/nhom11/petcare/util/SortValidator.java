package yoot.nhom11.petcare.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

public class SortValidator {

    public static void validateSort(Pageable pageable, Set<String> allowedFields) {
        if (pageable == null || pageable.getSort() == null) {
            return;
        }
        for (Sort.Order order : pageable.getSort()) {
            if (!allowedFields.contains(order.getProperty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sorting by field '" + order.getProperty() + "' is not supported. Allowed fields: " + allowedFields
                );
            }
        }
    }
}
