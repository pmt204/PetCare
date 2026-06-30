package yoot.nhom11.petcare.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.TestResult;
import yoot.nhom11.petcare.entity.MedicalRecord;
import yoot.nhom11.petcare.entity.MedicalRecordStatus;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.PetSpecies;
import yoot.nhom11.petcare.entity.Prescription;
import yoot.nhom11.petcare.entity.UserRole;

@SpringBootTest
@Transactional
class MedicalRecordControllerTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private Long petId;
	private Long medicalRecordId;

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

		AppUser vet = AppUser.builder()
				.username("vet1")
				.email("vet1@example.com")
				.passwordHash("password")
				.fullName("Vet One")
				.role(UserRole.VET)
				.active(true)
				.build();

		entityManager.persist(owner);
		entityManager.persist(vet);

		Pet pet = Pet.builder()
				.owner(owner)
				.name("Milo")
				.species(PetSpecies.DOG)
				.breed("Poodle")
				.avatarUrl(null)
				.build();
		entityManager.persist(pet);

		MedicalRecord record = MedicalRecord.builder()
				.pet(pet)
				.veterinarian(vet)
				.visitAt(Instant.parse("2026-06-23T08:30:00Z"))
				.status(MedicalRecordStatus.COMPLETED)
				.reasonForVisit("Mild cough")
				.diagnosis("Mild respiratory infection")
				.treatmentNote("Give medicine for 5 days")
				.followUpInstruction("Return if fever appears")
				.build();
		entityManager.persist(record);

		Prescription prescription = Prescription.builder()
				.medicalRecord(record)
				.medicationName("Amoxicillin")
				.dosage("250mg")
				.frequency("2 times/day")
				.durationDays(5)
				.instructions("Take after meals")
				.build();
		entityManager.persist(prescription);

		TestResult testResult = TestResult.builder()
				.medicalRecord(record)
				.testName("Blood test")
				.fileName("blood-test.pdf")
				.pdfUrl("/files/blood-test.pdf")
				.mimeType("application/pdf")
				.note("Normal result")
				.build();
		entityManager.persist(testResult);

		MedicalRecord draftRecord = MedicalRecord.builder()
				.pet(pet)
				.veterinarian(vet)
				.visitAt(Instant.parse("2026-06-24T08:30:00Z"))
				.status(MedicalRecordStatus.DRAFT)
				.reasonForVisit("Routine vaccination")
				.diagnosis("Pending")
				.treatmentNote("Draft note")
				.followUpInstruction("Return later")
				.build();
		entityManager.persist(draftRecord);

		entityManager.flush();
		entityManager.clear();

		petId = pet.getId();
		medicalRecordId = record.getId();
	}

	@Test
	void getPetTimeline_returnsPage() throws Exception {
		mockMvc.perform(get("/api/pets/{petId}/medical-records", petId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.content[0].status").value("DRAFT"))
				.andExpect(jsonPath("$.content[1].id").value(medicalRecordId))
				.andExpect(jsonPath("$.content[1].diagnosis").value("Mild respiratory infection"))
				.andExpect(jsonPath("$.content[1].prescriptionCount").value(1))
				.andExpect(jsonPath("$.content[1].labResultCount").value(1))
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(10));
	}

	@Test
	void getMedicalRecordDetail_returnsDetail() throws Exception {
		mockMvc.perform(get("/api/medical-records/{recordId}", medicalRecordId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(medicalRecordId))
				.andExpect(jsonPath("$.pet.name").value("Milo"))
				.andExpect(jsonPath("$.veterinarian.fullName").value("Vet One"))
				.andExpect(jsonPath("$.prescriptionItems[0].medicationName").value("Amoxicillin"))
				.andExpect(jsonPath("$.labResults[0].fileName").value("blood-test.pdf"));
	}

	@Test
	void getPetTimeline_filtersByStatusAndKeyword() throws Exception {
		mockMvc.perform(get("/api/pets/{petId}/medical-records", petId)
						.param("status", "COMPLETED")
						.param("keyword", "infection"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.content[0].status").value("COMPLETED"));
	}
}
