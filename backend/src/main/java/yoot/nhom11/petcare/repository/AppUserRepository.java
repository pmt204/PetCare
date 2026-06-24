package yoot.nhom11.petcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.UserRole;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	List<AppUser> findAllByRoleAndActiveTrue(UserRole role);

	Optional<AppUser> findByUsername(String username);

	Optional<AppUser> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
