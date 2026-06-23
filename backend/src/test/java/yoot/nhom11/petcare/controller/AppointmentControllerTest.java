package yoot.nhom11.petcare.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.PetSpecies;
import yoot.nhom11.petcare.entity.UserRole;

@SpringBootTest
@Transactional
class AppointmentControllerTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private Long ownerId;
	private Long petId;
	private Long vetId;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		AppUser owner = AppUser.builder()
				.username("owner1")
				.email("owner1@example.com")
				.passwordHash("password")
				.fullName("Owner One")
				.role(UserRole.OWNER)
				.active(true)
				.build();
		entityManager.persist(owner);

		AppUser vet = AppUser.builder()
				.username("vet1")
				.email("vet1@example.com")
				.passwordHash("password")
				.fullName("Vet One")
				.role(UserRole.VET)
				.active(true)
				.build();
		entityManager.persist(vet);

		Pet pet = Pet.builder()
				.owner(owner)
				.name("Milo")
				.species(PetSpecies.DOG)
				.breed("Poodle")
				.build();
		entityManager.persist(pet);

		entityManager.flush();
		entityManager.clear();

		ownerId = owner.getId();
		petId = pet.getId();
		vetId = vet.getId();
	}

	@Test
	void getFormData_returnsPetsAndVets() throws Exception {
		mockMvc.perform(get("/api/appointments/form-data").param("ownerId", ownerId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pets[0].id").value(petId))
				.andExpect(jsonPath("$.pets[0].name").value("Milo"))
				.andExpect(jsonPath("$.veterinarians[0].id").value(vetId))
				.andExpect(jsonPath("$.veterinarians[0].fullName").value("Vet One"));
	}

	@Test
	void createAppointment_createsRequestedAppointment() throws Exception {
		String body = """
				{
				  "ownerId": %d,
				  "petId": %d,
				  "veterinarianId": %d,
				  "appointmentAt": "2026-06-30T09:30:00",
				  "reasonForVisit": "General checkup"
				}
				""".formatted(ownerId, petId, vetId);

		mockMvc.perform(post("/api/appointments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.owner.id").value(ownerId))
				.andExpect(jsonPath("$.pet.id").value(petId))
				.andExpect(jsonPath("$.veterinarian.id").value(vetId))
				.andExpect(jsonPath("$.reasonForVisit").value("General checkup"))
				.andExpect(jsonPath("$.status").value("REQUESTED"));

		mockMvc.perform(get("/api/appointments").param("ownerId", ownerId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].pet.name").value("Milo"));
	}
}
