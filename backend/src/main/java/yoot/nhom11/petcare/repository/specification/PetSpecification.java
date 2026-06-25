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
                        criteriaBuilder.lower(root.get("name")),
                        searchPattern
                ));
            }

            if (StringUtils.hasText(filter.getPetType())) {
                try {
                    yoot.nhom11.petcare.entity.PetSpecies speciesEnum = yoot.nhom11.petcare.entity.PetSpecies.valueOf(filter.getPetType().trim().toUpperCase());
                    predicates.add(criteriaBuilder.equal(
                            root.get("species"),
                            speciesEnum
                    ));
                } catch (IllegalArgumentException e) {
                    // Ignore or match nothing
                }
            }

            if (StringUtils.hasText(filter.getPetGender())) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("gender")),
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

            if (filter.getOwnerId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("owner").get("id"),
                        filter.getOwnerId()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
