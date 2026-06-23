package yoot.nhom11.petcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

	Optional<Pet> findByIdAndOwnerId(Long id, Long ownerId);

	List<Pet> findAllByOwnerIdOrderByNameAsc(Long ownerId);
}
