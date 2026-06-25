package yoot.nhom11.petcare.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import yoot.nhom11.petcare.exception.BusinessException;
import yoot.nhom11.petcare.exception.ErrorCode;

import java.util.Map;
import java.util.Set;

public class SortValidator {

    public static void validateSort(Pageable pageable, Set<String> allowedFields) {
        if (pageable == null || pageable.getSort() == null) {
            return;
        }
        for (Sort.Order order : pageable.getSort()) {
            if (!allowedFields.contains(order.getProperty())) {
                Map<String, Object> details = Map.of(
                        "invalidField", order.getProperty(),
                        "allowedFields", allowedFields
                );
                throw new BusinessException(
                        ErrorCode.SORT_FIELD_INVALID,
                        "Sorting by field '" + order.getProperty() + "' is not supported. Allowed fields: " + allowedFields,
                        details
                );
            }
        }
    }
}
