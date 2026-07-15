package yoot.nhom11.petcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import yoot.nhom11.petcare.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {

	Optional<Pet> findByIdAndOwnerId(Long id, Long ownerId);

	List<Pet> findAllByOwnerIdOrderByNameAsc(Long ownerId);

	Optional<Pet> findBySlug(String slug);
}
