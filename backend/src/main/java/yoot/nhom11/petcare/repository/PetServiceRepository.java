package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoot.nhom11.petcare.entity.PetService;

public interface PetServiceRepository extends JpaRepository<PetService, Long> {
}
