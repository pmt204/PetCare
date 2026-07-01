package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoot.nhom11.petcare.entity.Invoice;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.appointment app LEFT JOIN FETCH app.doctor LEFT JOIN FETCH app.owner LEFT JOIN FETCH app.pet LEFT JOIN FETCH i.services")
    List<Invoice> findAllWithDetails();
}
