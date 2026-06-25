package yoot.nhom11.petcare.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import yoot.nhom11.petcare.dto.request.PetFilterRequest;
import yoot.nhom11.petcare.entity.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetSpecification {

    public static Specification<Pet> filterPets(PetFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filter.getSearch())) {
                String searchPattern = "%" + filter.getSearch().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("petName")),
                        searchPattern
                ));
            }

            if (StringUtils.hasText(filter.getPetType())) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("petType")),
                        filter.getPetType().trim().toLowerCase()
                ));
            }

            if (StringUtils.hasText(filter.getPetGender())) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("petGender")),
                        filter.getPetGender().trim().toLowerCase()
                ));
            }

            if (filter.getMinAge() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("petAge"),
                        filter.getMinAge()
                ));
            }

            if (filter.getMaxAge() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("petAge"),
                        filter.getMaxAge()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
