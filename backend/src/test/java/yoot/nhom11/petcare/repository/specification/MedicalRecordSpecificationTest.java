package yoot.nhom11.petcare.repository.specification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import yoot.nhom11.petcare.dto.request.MedicalRecordFilterRequest;
import yoot.nhom11.petcare.entity.MedicalRecord;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked", "rawtypes"})
class MedicalRecordSpecificationTest {

    @Mock
    private Root<MedicalRecord> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> path;

    @Mock
    private Join<Object, Object> petJoin;

    @Mock
    private Join<Object, Object> billJoin;

    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        lenient().when(criteriaBuilder.conjunction()).thenReturn(predicate);
        lenient().when(criteriaBuilder.disjunction()).thenReturn(predicate);
        lenient().when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.or(any(Predicate[].class))).thenReturn(predicate);
    }

    @Test
    void filterMedicalRecords_nullFilter_returnsConjunction() {
        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterMedicalRecords(null);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).conjunction();
    }

    @Test
    void filterMedicalRecords_searchFilter() {
        MedicalRecordFilterRequest filter = MedicalRecordFilterRequest.builder()
                .search("fever")
                .build();

        Path<String> diagnosisExpr = mock(Path.class);
        Path<String> treatmentExpr = mock(Path.class);
        Expression<String> lowerDiag = mock(Expression.class);
        Expression<String> lowerTreat = mock(Expression.class);

        when(root.get("diagnosis")).thenReturn((Path) diagnosisExpr);
        when(root.get("treatment")).thenReturn((Path) treatmentExpr);
        when(criteriaBuilder.lower(diagnosisExpr)).thenReturn(lowerDiag);
        when(criteriaBuilder.lower(treatmentExpr)).thenReturn(lowerTreat);
        when(criteriaBuilder.like(eq(lowerDiag), eq("%fever%"))).thenReturn(predicate);
        when(criteriaBuilder.like(eq(lowerTreat), eq("%fever%"))).thenReturn(predicate);
        when(criteriaBuilder.or(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);

        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterMedicalRecords(filter);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).or(any(Predicate.class), any(Predicate.class));
    }

    @Test
    void filterMedicalRecords_petIdFilter() {
        MedicalRecordFilterRequest filter = MedicalRecordFilterRequest.builder()
                .petId(5)
                .build();

        when(root.join(eq("pet"), eq(JoinType.LEFT))).thenReturn((Join) petJoin);
        when(petJoin.get("petId")).thenReturn(path);
        when(criteriaBuilder.equal(eq(path), eq(5))).thenReturn(predicate);

        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterMedicalRecords(filter);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(eq(path), eq(5));
    }

    @Test
    void filterMedicalRecords_billStatusFilter() {
        MedicalRecordFilterRequest filter = MedicalRecordFilterRequest.builder()
                .billStatus("PAID")
                .build();

        Path<String> statusExpr = mock(Path.class);
        Expression<String> lowerStatus = mock(Expression.class);

        when(root.join(eq("bill"), eq(JoinType.LEFT))).thenReturn((Join) billJoin);
        when(billJoin.get("status")).thenReturn((Path) statusExpr);
        when(criteriaBuilder.lower(statusExpr)).thenReturn(lowerStatus);
        when(criteriaBuilder.equal(eq(lowerStatus), eq("paid"))).thenReturn(predicate);

        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterMedicalRecords(filter);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(eq(lowerStatus), eq("paid"));
    }

    @Test
    void filterMedicalRecords_dateRangeFilter() {
        MedicalRecordFilterRequest filter = MedicalRecordFilterRequest.builder()
                .dateFrom(LocalDate.of(2026, 6, 25))
                .dateTo(LocalDate.of(2026, 6, 26))
                .build();

        Path<Date> datePath = mock(Path.class);
        when(root.get("date")).thenReturn((Path) datePath);
        when(criteriaBuilder.greaterThanOrEqualTo(eq(datePath), any(Date.class))).thenReturn(predicate);
        when(criteriaBuilder.lessThan(eq(datePath), any(Date.class))).thenReturn(predicate);

        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterMedicalRecords(filter);
        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThanOrEqualTo(eq(datePath), any(Date.class));
        verify(criteriaBuilder).lessThan(eq(datePath), any(Date.class));
    }
}
