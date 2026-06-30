package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoot.nhom11.petcare.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
