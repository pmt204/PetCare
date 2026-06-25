package yoot.nhom11.petcare.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.entity.Bill;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.entity.Pet;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicalRecordSpecification {

    public static Specification<MedicalRecord> filterMedicalRecords(MedicalRecordFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filter.getSearch())) {
                String searchPattern = "%" + filter.getSearch().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("diagnosis")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("treatment")), searchPattern)
                ));
            }

            if (filter.getPetId() != null) {
                Join<MedicalRecord, Pet> petJoin = root.join("pet", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(petJoin.get("petId"), filter.getPetId()));
            }

            if (StringUtils.hasText(filter.getBillStatus())) {
                Join<MedicalRecord, Bill> billJoin = root.join("bill", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(billJoin.get("status")),
                        filter.getBillStatus().trim().toLowerCase()
                ));
            }

            if (filter.getDateFrom() != null) {
                Date startOfDay = Date.from(filter.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startOfDay));
            }

            if (filter.getDateTo() != null) {
                Date startOfNextDay = Date.from(filter.getDateTo().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                predicates.add(criteriaBuilder.lessThan(root.get("date"), startOfNextDay));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
