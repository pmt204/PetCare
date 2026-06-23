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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.LabResult;
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
				.reasonForVisit("Ho nhẹ")
				.diagnosis("Viêm hô hấp nhẹ")
				.treatmentNote("Cho uống thuốc 5 ngày")
				.followUpInstruction("Tái khám nếu sốt")
				.build();
		entityManager.persist(record);

		Prescription prescription = Prescription.builder()
				.medicalRecord(record)
				.medicationName("Amoxicillin")
				.dosage("250mg")
				.frequency("2 lần/ngày")
				.durationDays(5)
				.instructions("Uống sau ăn")
				.build();
		entityManager.persist(prescription);

		LabResult labResult = LabResult.builder()
				.medicalRecord(record)
				.title("Xét nghiệm máu")
				.fileName("blood-test.pdf")
				.fileUrl("/files/blood-test.pdf")
				.mimeType("application/pdf")
				.note("Kết quả bình thường")
				.build();
		entityManager.persist(labResult);

		entityManager.flush();
		entityManager.clear();

		petId = pet.getId();
		medicalRecordId = record.getId();
	}

	@Test
	void getPetTimeline_returnsPage() throws Exception {
		mockMvc.perform(get("/api/pets/{petId}/medical-records", petId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(medicalRecordId))
				.andExpect(jsonPath("$.content[0].diagnosis").value("Viêm hô hấp nhẹ"))
				.andExpect(jsonPath("$.content[0].prescriptionCount").value(1))
				.andExpect(jsonPath("$.content[0].labResultCount").value(1))
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
				.andExpect(jsonPath("$.prescriptions[0].medicationName").value("Amoxicillin"))
				.andExpect(jsonPath("$.labResults[0].fileName").value("blood-test.pdf"));
	}
}
