package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoot.nhom11.petcare.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
