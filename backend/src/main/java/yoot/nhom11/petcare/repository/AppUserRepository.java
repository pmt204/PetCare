package yoot.nhom11.petcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.UserRole;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	List<AppUser> findAllByRoleAndActiveTrue(UserRole role);
}
