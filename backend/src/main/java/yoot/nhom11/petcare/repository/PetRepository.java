package yoot.nhom11.petcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoot.nhom11.petcare.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
}
