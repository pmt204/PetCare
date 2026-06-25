package yoot.nhom11.petcare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.UserRole;
import yoot.nhom11.petcare.repository.AppUserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (appUserRepository.count() == 0) {
			// Seed Admin
			AppUser admin = AppUser.builder()
					.username("admin")
					.email("admin@petcare.com")
					.passwordHash(passwordEncoder.encode("admin123"))
					.fullName("Administrator")
					.role(UserRole.ADMIN)
					.active(true)
					.build();
			appUserRepository.save(admin);

			// Seed Vet
			AppUser vet = AppUser.builder()
					.username("vet")
					.email("vet@petcare.com")
					.passwordHash(passwordEncoder.encode("vet123"))
					.fullName("Dr. John Doe")
					.role(UserRole.VET)
					.active(true)
					.build();
			appUserRepository.save(vet);

			// Seed Owner
			AppUser owner = AppUser.builder()
					.username("owner")
					.email("owner@petcare.com")
					.passwordHash(passwordEncoder.encode("owner123"))
					.fullName("Pet Owner")
					.role(UserRole.OWNER)
					.active(true)
					.build();
			appUserRepository.save(owner);

			System.out.println("Default users (admin, vet, owner) seeded successfully!");
		}
	}
}
