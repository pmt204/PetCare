package yoot.nhom11.petcare.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import yoot.nhom11.petcare.entity.*;
import yoot.nhom11.petcare.repository.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private PetServiceRepository petServiceRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// 1. Seed or Update Default Users
		String[] defaultUsernames = {"admin", "vet", "vet2", "vet3", "owner", "owner2"};
		String[] defaultEmails = {"admin@petcare.com", "vet@petcare.com", "vet2@petcare.com", "vet3@petcare.com", "owner@petcare.com", "owner2@petcare.com"};
		String[] defaultFullNames = {"Administrator", "Dr. John Doe", "Dr. Sarah Conner", "Dr. Helen Carter", "Pet Owner", "Alice Johnson"};
		UserRole[] defaultRoles = {UserRole.ADMIN, UserRole.VET, UserRole.VET, UserRole.VET, UserRole.OWNER, UserRole.OWNER};
		String[] defaultPasswords = {"admin123", "vet123", "vet123", "vet123", "owner123", "owner123"};

		for (int i = 0; i < defaultUsernames.length; i++) {
			final int index = i;
			AppUser user = appUserRepository.findByUsername(defaultUsernames[i]).orElseGet(() -> {
				AppUser u = new AppUser();
				u.setUsername(defaultUsernames[index]);
				u.setCreatedAt(Instant.now());
				return u;
			});
			user.setEmail(defaultEmails[i]);
			user.setFullName(defaultFullNames[i]);
			user.setRole(defaultRoles[i]);
			user.setActive(true);
			user.setPasswordHash(passwordEncoder.encode(defaultPasswords[i]));
			user.setUpdatedAt(Instant.now());
			if (i == 4) { // owner
				user.setPhone("0912345678");
				user.setAddress("123 Main St, Hanoi");
			} else if (i == 5) { // owner2
				user.setPhone("0987654321");
				user.setAddress("456 Park Ave, HCM City");
			}
			appUserRepository.save(user);
		}
		System.out.println("Default users seeded/updated successfully!");

		AppUser owner = appUserRepository.findByUsername("owner").orElse(null);
		AppUser owner2 = appUserRepository.findByUsername("owner2").orElse(null);
		AppUser vet = appUserRepository.findByUsername("vet").orElse(null);
		AppUser vet2 = appUserRepository.findByUsername("vet2").orElse(null);

		// 2. Seed / Update Doctors
		{
			List<Doctor> existingDoctors = doctorRepository.findAll();
			Doctor doc1 = existingDoctors.stream().filter(d -> d.getName().equals("Dr. John Doe")).findFirst().orElse(new Doctor());
			doc1.setName("Dr. John Doe");
			doc1.setSpecialty("Khám bệnh đa khoa & Nội khoa");
			doc1.setExperienceYears("5 năm");
			doc1.setImage("/images/doctor_john.jpg");
			doc1.setRating(4.8);
			doc1.setDescription("Chuyên gia chẩn đoán lâm sàng nội khoa, khám tổng quát và điều trị các bệnh truyền nhiễm thường gặp.");
			doc1.setFullDescription("Bác sĩ John Doe tốt nghiệp Đại học Y Dược ngành Thú y lâm sàng, có hơn 5 năm kinh nghiệm trong lĩnh vực khám tổng quát, chẩn đoán hình ảnh và đưa ra các phác đồ điều trị nội khoa chuẩn xác cho chó mèo.");
			doc1.setServices(new ArrayList<>(List.of("Khám bệnh lâm sàng tổng quát", "Khám sàng lọc sức khỏe trước tiêm", "Tư vấn phác đồ điều trị bệnh truyền nhiễm")));
			doctorRepository.save(doc1);

			Doctor doc2 = existingDoctors.stream()
					.filter(d -> d.getName().equals("Dr. Sarah Conner") || d.getName().equals("Dr. Jane Smith"))
					.findFirst()
					.orElse(new Doctor());
			doc2.setName("Dr. Sarah Conner");
			doc2.setSpecialty("Phẫu thuật ngoại khoa & Chấn thương");
			doc2.setExperienceYears("8 năm");
			doc2.setImage("/images/doctor_sarah.jpg");
			doc2.setRating(4.9);
			doc2.setDescription("Hơn 8 năm kinh nghiệm thực hiện các ca phẫu thuật phức tạp, chấn thương chỉnh hình và cấp cứu thú cưng.");
			doc2.setFullDescription("Bác sĩ Sarah Conner là một trong những bác sĩ ngoại khoa hàng đầu tại hệ thống PetCare. Bà đã hoàn thành khóa đào tạo chuyên sâu về chấn thương chỉnh hình thú cưng tại nước ngoài và có 8 năm kinh nghiệm thực hiện thành công hàng trăm ca phẫu thuật ngoại khoa phức tạp.");
			doc2.setServices(new ArrayList<>(List.of("Phẫu thuật triệt sản chó & mèo", "Phẫu thuật kết hợp xương chấn thương", "Cấp cứu ngoại khoa khẩn cấp")));
			doctorRepository.save(doc2);

			Doctor doc3 = existingDoctors.stream().filter(d -> d.getName().equals("Dr. Helen Carter")).findFirst().orElse(new Doctor());
			doc3.setName("Dr. Helen Carter");
			doc3.setSpecialty("Da liễu & Điều trị nội trú");
			doc3.setExperienceYears("6 năm");
			doc3.setImage("/images/doctor_helen.jpg");
			doc3.setRating(4.7);
			doc3.setDescription("Chuyên điều trị các bệnh về da liễu, nấm, dị ứng và theo dõi sức khỏe vật nuôi nằm viện dài ngày.");
			doc3.setFullDescription("Bác sĩ Helen Carter tốt nghiệp Thạc sĩ Y khoa thú y và cống hiến 6 năm qua trong lĩnh vực da liễu động vật. Bà nổi tiếng với sự tận tụy, nhẹ nhàng và khả năng xử lý dứt điểm các ca viêm da dị ứng mãn tính hoặc nấm ký sinh trùng dai dẳng.");
			doc3.setServices(new ArrayList<>(List.of("Khám soi da liễu tìm ký sinh trùng", "Điều trị viêm tai & nấm da mãn tính", "Theo dõi chăm sóc nội trú đặc biệt")));
			doctorRepository.save(doc3);

			System.out.println("Doctors seeded/updated successfully!");
		}

		// 3. Seed / Update PetServices
		{
			List<PetService> existingServices = petServiceRepository.findAll();
			
			PetService s1 = existingServices.stream()
					.filter(s -> s.getName().equals("General Checkup") || s.getName().equals("Khám bệnh tổng quát"))
					.findFirst().orElse(new PetService());
			s1.setName("Khám bệnh tổng quát");
			s1.setDescription("Đánh giá lâm sàng tình trạng tim mạch, hệ hô hấp, tai mắt miệng và kiểm tra phản xạ vận động để phát hiện các triệu chứng bất thường.");
			s1.setPrice(150000.0);
			petServiceRepository.save(s1);

			PetService s2 = existingServices.stream()
					.filter(s -> s.getName().equals("Vaccination Package") || s.getName().equals("Tiêm phòng vaccine"))
					.findFirst().orElse(new PetService());
			s2.setName("Tiêm phòng vaccine");
			s2.setDescription("Tiêm vaccine ngừa các bệnh truyền nhiễm nguy hiểm ở chó và mèo (dại, parvo, giảm bạch cầu...) kèm lập sổ theo dõi tiêm phòng định kỳ.");
			s2.setPrice(250000.0);
			petServiceRepository.save(s2);

			PetService s3 = existingServices.stream()
					.filter(s -> s.getName().equals("Surgical Sterilization") || s.getName().equals("Phẫu thuật ngoại khoa"))
					.findFirst().orElse(new PetService());
			s3.setName("Phẫu thuật ngoại khoa");
			s3.setDescription("Thực hiện các ca phẫu thuật triệt sản, mổ đẻ khẩn cấp, khâu vết thương sâu hoặc chấn thương xương khớp trong phòng mổ vô trùng đạt chuẩn.");
			s3.setPrice(1500000.0);
			petServiceRepository.save(s3);

			PetService s4 = existingServices.stream()
					.filter(s -> s.getName().equals("Blood Test & Diagnostics") || s.getName().equals("Xét nghiệm & Siêu âm"))
					.findFirst().orElse(new PetService());
			s4.setName("Xét nghiệm & Siêu âm");
			s4.setDescription("Phân tích tế bào máu, xét nghiệm sinh hóa gan thận và tiến hành siêu âm ổ bụng chuyên khoa giúp chẩn đoán bệnh chính xác nhất.");
			s4.setPrice(350000.0);
			petServiceRepository.save(s4);

			PetService s5 = existingServices.stream()
					.filter(s -> s.getName().equals("Dental Cleaning") || s.getName().equals("Nha khoa thú y"))
					.findFirst().orElse(new PetService());
			s5.setName("Nha khoa thú y");
			s5.setDescription("Lấy cao răng siêu âm an toàn, điều trị viêm nướu, nhổ răng sâu đau buốt giúp hơi thở thơm mát và cải thiện khả năng ăn nhai.");
			s5.setPrice(500000.0);
			petServiceRepository.save(s5);

			PetService s6 = existingServices.stream()
					.filter(s -> s.getName().equals("Điều trị nội trú theo dõi"))
					.findFirst().orElse(new PetService());
			s6.setName("Điều trị nội trú theo dõi");
			s6.setDescription("Chăm sóc đặc biệt 24/7 dành cho các trường hợp bệnh nặng, truyền dịch liên tục và theo dõi sát sao biểu hiện sinh tồn bởi bác sĩ trực.");
			s6.setPrice(400000.0);
			petServiceRepository.save(s6);

			System.out.println("Pet Services seeded/updated successfully!");
		}

		// 4. Seed Pets
		{
			List<Pet> existingPets = petRepository.findAll();
			
			Pet pet1 = existingPets.stream().filter(p -> p.getName().equals("LuLu")).findFirst().orElse(new Pet());
			pet1.setOwner(owner);
			pet1.setName("LuLu");
			pet1.setSpecies(PetSpecies.DOG);
			pet1.setBreed("Golden Retriever");
			pet1.setBirthDate(LocalDate.now().minusYears(2));
			pet1.setGender("Male");
			pet1.setSlug("lulu");
			pet1.setPetAge(2);
			pet1.setAvatarUrl("https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&q=80&w=200");
			pet1.setCreatedAt(Instant.now());
			pet1.setUpdatedAt(Instant.now());
			petRepository.save(pet1);

			Pet pet2 = existingPets.stream().filter(p -> p.getName().equals("MiMi")).findFirst().orElse(new Pet());
			pet2.setOwner(owner);
			pet2.setName("MiMi");
			pet2.setSpecies(PetSpecies.CAT);
			pet2.setBreed("Persian");
			pet2.setBirthDate(LocalDate.now().minusYears(1));
			pet2.setGender("Female");
			pet2.setSlug("mimi");
			pet2.setPetAge(1);
			pet2.setAvatarUrl("https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&q=80&w=200");
			pet2.setCreatedAt(Instant.now());
			pet2.setUpdatedAt(Instant.now());
			petRepository.save(pet2);

			Pet pet3 = existingPets.stream().filter(p -> p.getName().equals("Rocky")).findFirst().orElse(new Pet());
			pet3.setOwner(owner2);
			pet3.setName("Rocky");
			pet3.setSpecies(PetSpecies.DOG);
			pet3.setBreed("Siberian Husky");
			pet3.setBirthDate(LocalDate.now().minusYears(3));
			pet3.setGender("Male");
			pet3.setSlug("rocky");
			pet3.setPetAge(3);
			pet3.setAvatarUrl("https://images.unsplash.com/photo-1531804055935-76f44d7c3621?auto=format&fit=crop&q=80&w=200");
			pet3.setCreatedAt(Instant.now());
			pet3.setUpdatedAt(Instant.now());
			petRepository.save(pet3);

			Pet pet4 = existingPets.stream().filter(p -> p.getName().equals("Bông")).findFirst().orElse(new Pet());
			pet4.setOwner(owner2);
			pet4.setName("Bông");
			pet4.setSpecies(PetSpecies.DOG);
			pet4.setBreed("Poodle");
			pet4.setBirthDate(LocalDate.now().minusMonths(18));
			pet4.setGender("Female");
			pet4.setSlug("bong");
			pet4.setPetAge(1);
			pet4.setAvatarUrl("https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?auto=format&fit=crop&q=80&w=200");
			pet4.setCreatedAt(Instant.now());
			pet4.setUpdatedAt(Instant.now());
			petRepository.save(pet4);

			Pet pet5 = existingPets.stream().filter(p -> p.getName().equals("Simba")).findFirst().orElse(new Pet());
			pet5.setOwner(owner);
			pet5.setName("Simba");
			pet5.setSpecies(PetSpecies.CAT);
			pet5.setBreed("British Shorthair");
			pet5.setBirthDate(LocalDate.now().minusYears(2));
			pet5.setGender("Male");
			pet5.setSlug("simba");
			pet5.setPetAge(2);
			pet5.setAvatarUrl("https://images.unsplash.com/photo-1573865526739-10659fec78a5?auto=format&fit=crop&q=80&w=200");
			pet5.setCreatedAt(Instant.now());
			pet5.setUpdatedAt(Instant.now());
			petRepository.save(pet5);

			System.out.println("Pets seeded/updated successfully!");
		}

		// 5. Seed Medicines
		{
			List<Medicine> existingMeds = medicineRepository.findAll();
			
			Medicine med1 = existingMeds.stream().filter(m -> m.getMedicineName().equals("Amoxicillin")).findFirst().orElse(new Medicine());
			med1.setMedicineName("Amoxicillin");
			med1.setUnit("tablets");
			med1.setDescription("Antibiotic for bacterial infections in pets.");
			med1.setCreateAt(LocalDateTime.now());
			med1.setUpdateAt(LocalDateTime.now());
			med1.setCreateBy("System");
			med1.setUpdateBy("System");
			medicineRepository.save(med1);

			Medicine med2 = existingMeds.stream().filter(m -> m.getMedicineName().equals("Paracetamol Vet")).findFirst().orElse(new Medicine());
			med2.setMedicineName("Paracetamol Vet");
			med2.setUnit("syrup");
			med2.setDescription("Pain relief and fever reducer formula for canines.");
			med2.setCreateAt(LocalDateTime.now());
			med2.setUpdateAt(LocalDateTime.now());
			med2.setCreateBy("System");
			med2.setUpdateBy("System");
			medicineRepository.save(med2);

			Medicine med3 = existingMeds.stream().filter(m -> m.getMedicineName().equals("Ivermectin")).findFirst().orElse(new Medicine());
			med3.setMedicineName("Ivermectin");
			med3.setUnit("tablets");
			med3.setDescription("Effective dewormer and parasite control.");
			med3.setCreateAt(LocalDateTime.now());
			med3.setUpdateAt(LocalDateTime.now());
			med3.setCreateBy("System");
			med3.setUpdateBy("System");
			medicineRepository.save(med3);

			Medicine med4 = existingMeds.stream().filter(m -> m.getMedicineName().equals("Dewormer Max")).findFirst().orElse(new Medicine());
			med4.setMedicineName("Dewormer Max");
			med4.setUnit("tablets");
			med4.setDescription("Broad-spectrum dewormer for cats and dogs.");
			med4.setCreateAt(LocalDateTime.now());
			med4.setUpdateAt(LocalDateTime.now());
			med4.setCreateBy("System");
			med4.setUpdateBy("System");
			medicineRepository.save(med4);

			Medicine med5 = existingMeds.stream().filter(m -> m.getMedicineName().equals("Ear Clean Pro")).findFirst().orElse(new Medicine());
			med5.setMedicineName("Ear Clean Pro");
			med5.setUnit("drops");
			med5.setDescription("Eardrops for treating ear infections and mites.");
			med5.setCreateAt(LocalDateTime.now());
			med5.setUpdateAt(LocalDateTime.now());
			med5.setCreateBy("System");
			med5.setUpdateBy("System");
			medicineRepository.save(med5);

			System.out.println("Medicines seeded/updated successfully!");
		}

		// 6. Seed Appointments & Invoices & Medical Records & Prescriptions
		{
			List<Appointment> existingApps = appointmentRepository.findAll();
			
			Pet lulu = petRepository.findAll().stream().filter(p -> p.getName().equals("LuLu")).findFirst().orElse(null);
			Pet mimi = petRepository.findAll().stream().filter(p -> p.getName().equals("MiMi")).findFirst().orElse(null);
			Pet rocky = petRepository.findAll().stream().filter(p -> p.getName().equals("Rocky")).findFirst().orElse(null);
			Pet bong = petRepository.findAll().stream().filter(p -> p.getName().equals("Bông")).findFirst().orElse(null);
			Pet simba = petRepository.findAll().stream().filter(p -> p.getName().equals("Simba")).findFirst().orElse(null);

			List<PetService> services = petServiceRepository.findAll();
			PetService checkupService = services.stream().filter(s -> s.getName().equals("Khám bệnh tổng quát")).findFirst().orElse(null);
			PetService vaccineService = services.stream().filter(s -> s.getName().equals("Tiêm phòng vaccine")).findFirst().orElse(null);
			PetService surgService = services.stream().filter(s -> s.getName().equals("Phẫu thuật ngoại khoa")).findFirst().orElse(null);
			PetService scanService = services.stream().filter(s -> s.getName().equals("Xét nghiệm & Siêu âm")).findFirst().orElse(null);

			List<Medicine> medicines = medicineRepository.findAll();
			Medicine amox = medicines.stream().filter(m -> m.getMedicineName().equals("Amoxicillin")).findFirst().orElse(null);
			Medicine earClean = medicines.stream().filter(m -> m.getMedicineName().equals("Ear Clean Pro")).findFirst().orElse(null);
			Medicine dewormer = medicines.stream().filter(m -> m.getMedicineName().equals("Dewormer Max")).findFirst().orElse(null);

			Doctor doc1 = doctorRepository.findAll().stream().filter(d -> d.getName().equals("Dr. John Doe")).findFirst().orElse(null);
			Doctor doc2 = doctorRepository.findAll().stream().filter(d -> d.getName().equals("Dr. Sarah Conner")).findFirst().orElse(null);
			Doctor doc3 = doctorRepository.findAll().stream().filter(d -> d.getName().equals("Dr. Helen Carter")).findFirst().orElse(null);

			// --- Lịch hẹn 1 (Đã hoàn thành, 5 ngày trước) ---
			Appointment app1 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("LuLu") && a.getReason().equals("Routine Checkup & Vaccination")).findFirst().orElse(null);
			if (app1 == null && lulu != null) {
				app1 = new Appointment();
				app1.setOwner(owner);
				app1.setPet(lulu);
				app1.setVeterinarian(vet);
				app1.setDoctor(doc1);
				app1.setAppointmentAt(LocalDateTime.now().minusDays(5));
				app1.setAppointmentTime(LocalDateTime.now().minusDays(5));
				app1.setReasonForVisit("Routine Checkup & Vaccination");
				app1.setReason("Routine Checkup & Vaccination");
				app1.setStatus(AppointmentStatus.CONFIRMED);
				app1.setPatientName(lulu.getName());
				app1.setPatientPhone(owner != null ? owner.getPhone() : "");
				app1.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
				app1.setUpdatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
				appointmentRepository.save(app1);

				// Hóa đơn 1 (Đã thanh toán)
				Invoice inv1 = new Invoice();
				inv1.setAppointment(app1);
				inv1.setServices(new ArrayList<>(List.of(checkupService, vaccineService)));
				inv1.setTotalAmount(checkupService.getPrice() + vaccineService.getPrice());
				inv1.setPaymentStatus(PaymentStatus.PAID);
				inv1.setCreatedAt(LocalDateTime.now().minusDays(5));
				invoiceRepository.save(inv1);

				// Bệnh án 1
				MedicalRecord rec1 = new MedicalRecord();
				rec1.setPet(lulu);
				rec1.setVeterinarian(vet);
				rec1.setDoctor(doc1);
				rec1.setVisitAt(Instant.now().minus(5, ChronoUnit.DAYS));
				rec1.setStatus(MedicalRecordStatus.COMPLETED);
				rec1.setReasonForVisit("Routine Checkup & Vaccination");
				rec1.setDiagnosis("Healthy Retriever, completed annual vaccines.");
				rec1.setTreatmentNote("Vaccinated with DHPP vaccine. Cleaned ears.");
				rec1.setFollowUpInstruction("Monitor injection site for swelling. Keep quiet for 24h.");
				rec1.setNextVisitDate(LocalDate.now().plusYears(1));
				rec1.setPatientName(lulu.getName());
				rec1.setSymptoms("Normal appetite, energetic, no coughing.");
				rec1.setNotes("Lulu behaves well during examination.");
				rec1.setCreatedDate(LocalDateTime.now().minusDays(5));
				rec1.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
				rec1.setUpdatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
				rec1.setCreateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
				rec1.setUpdateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
				rec1.setInvoice(inv1);

				if (amox != null) {
					Prescription p = new Prescription();
					p.setMedicalRecord(rec1);
					p.setMedicationName(amox.getMedicineName());
					p.setDosage("1 tablet");
					p.setFrequency("twice daily");
					p.setDurationDays(5);
					p.setInstructions("Take after meals.");
					p.setQuantity(10);
					p.setMedicine(amox);
					p.setDoctor(doc1);
					p.setPatientName(lulu.getName());
					p.setCreatedDate(LocalDateTime.now().minusDays(5));
					p.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
					p.setUpdatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
					p.setCreateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
					p.setUpdateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
					p.setStatus("Active");
					rec1.getPrescriptions().add(p);
				}
				medicalRecordRepository.save(rec1);
			}

			// --- Lịch hẹn 2 (Đang chờ khám, 2 ngày tới) ---
			Appointment app2 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("MiMi") && a.getReason().equals("Surgical consultation (Spaying)")).findFirst().orElse(null);
			if (app2 == null && mimi != null) {
				app2 = new Appointment();
				app2.setOwner(owner);
				app2.setPet(mimi);
				app2.setVeterinarian(vet2);
				app2.setDoctor(doc2);
				app2.setAppointmentAt(LocalDateTime.now().plusDays(2));
				app2.setAppointmentTime(LocalDateTime.now().plusDays(2));
				app2.setReasonForVisit("Surgical consultation (Spaying)");
				app2.setReason("Surgical consultation (Spaying)");
				app2.setStatus(AppointmentStatus.CONFIRMED);
				app2.setPatientName(mimi.getName());
				app2.setPatientPhone(owner != null ? owner.getPhone() : "");
				app2.setCreatedAt(Instant.now());
				app2.setUpdatedAt(Instant.now());
				appointmentRepository.save(app2);

				// Hóa đơn 2 (Chưa thanh toán)
				Invoice inv2 = new Invoice();
				inv2.setAppointment(app2);
				inv2.setServices(new ArrayList<>(List.of(surgService)));
				inv2.setTotalAmount(surgService.getPrice());
				inv2.setPaymentStatus(PaymentStatus.UNPAID);
				inv2.setCreatedAt(LocalDateTime.now());
				invoiceRepository.save(inv2);
			}

			// --- Lịch hẹn 3 (Chưa duyệt, 1 ngày tới) ---
			Appointment app3 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("Rocky") && a.getReason().equals("General Checkup - Coughing")).findFirst().orElse(null);
			if (app3 == null && rocky != null) {
				app3 = new Appointment();
				app3.setOwner(owner2);
				app3.setPet(rocky);
				app3.setVeterinarian(vet);
				app3.setDoctor(doc1);
				app3.setAppointmentAt(LocalDateTime.now().plusDays(1));
				app3.setAppointmentTime(LocalDateTime.now().plusDays(1));
				app3.setReasonForVisit("General Checkup - Coughing");
				app3.setReason("General Checkup - Coughing");
				app3.setStatus(AppointmentStatus.REQUESTED);
				app3.setPatientName(rocky.getName());
				app3.setPatientPhone(owner2 != null ? owner2.getPhone() : "");
				app3.setCreatedAt(Instant.now());
				app3.setUpdatedAt(Instant.now());
				appointmentRepository.save(app3);
			}

			// --- Lịch hẹn 4 (Hôm qua, đã khám, đã thanh toán) ---
			Appointment app4 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("Simba") && a.getReason().equals("Tai bị ngứa đỏ")).findFirst().orElse(null);
			if (app4 == null && simba != null) {
				app4 = new Appointment();
				app4.setOwner(owner);
				app4.setPet(simba);
				app4.setVeterinarian(vet);
				app4.setDoctor(doc1); // Dr. John Doe

				app4.setAppointmentAt(LocalDateTime.now().minusDays(1));
				app4.setAppointmentTime(LocalDateTime.now().minusDays(1));
				app4.setReasonForVisit("Tai bị ngứa đỏ");
				app4.setReason("Tai bị ngứa đỏ");
				app4.setStatus(AppointmentStatus.CONFIRMED);

				app4.setPatientName(simba.getName());
				app4.setPatientPhone(owner != null ? owner.getPhone() : "");
				app4.setCreatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
				app4.setUpdatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
				appointmentRepository.save(app4);

				// Hóa đơn 4 (Đã thanh toán)
				Invoice inv4 = new Invoice();
				inv4.setAppointment(app4);
				inv4.setServices(new ArrayList<>(List.of(checkupService)));
				inv4.setTotalAmount(checkupService.getPrice());
				inv4.setPaymentStatus(PaymentStatus.PAID);
				inv4.setCreatedAt(LocalDateTime.now().minusDays(1));
				invoiceRepository.save(inv4);

				// Bệnh án 4
				MedicalRecord rec4 = new MedicalRecord();
				rec4.setPet(simba);
				rec4.setVeterinarian(vet);
				rec4.setDoctor(doc1);

				rec4.setVisitAt(Instant.now().minus(1, ChronoUnit.DAYS));
				rec4.setStatus(MedicalRecordStatus.COMPLETED);
				rec4.setReasonForVisit("Tai bị ngứa đỏ");
				rec4.setDiagnosis("Bị nhiễm rận tai & viêm tai nhẹ.");
				rec4.setTreatmentNote("Vệ sinh sạch tai bằng dung dịch chuyên dụng. Nhỏ thuốc Ear Clean Pro.");
				rec4.setFollowUpInstruction("Nhỏ tai hàng ngày 2 lần. Tránh để nước vào tai khi tắm.");
				rec4.setNextVisitDate(LocalDate.now().plusDays(10));
				rec4.setPatientName(simba.getName());
				rec4.setSymptoms("Ngứa tai, gãi liên tục, có nhiều dịch màu nâu đen trong tai.");
				rec4.setNotes("Mèo Simba rất hợp tác.");
				rec4.setCreatedDate(LocalDateTime.now().minusDays(1));
				rec4.setCreatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
				rec4.setUpdatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
				rec4.setCreateBy(vet != null ? vet.getFullName() : "Dr. Helen Carter");
				rec4.setUpdateBy(vet != null ? vet.getFullName() : "Dr. Helen Carter");
				rec4.setInvoice(inv4);

				if (earClean != null) {
					Prescription p = new Prescription();
					p.setMedicalRecord(rec4);
					p.setMedicationName(earClean.getMedicineName());
					p.setDosage("Nhỏ 3 giọt mỗi tai");
					p.setFrequency("ngày 2 lần");
					p.setDurationDays(7);
					p.setInstructions("Lắc đều trước khi nhỏ, lau sạch dịch tai trước.");
					p.setQuantity(1);
					p.setMedicine(earClean);
					p.setDoctor(doc1);

					p.setPatientName(simba.getName());
					p.setCreatedDate(LocalDateTime.now().minusDays(1));
					p.setCreatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
					p.setUpdatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
					p.setCreateBy(vet != null ? vet.getFullName() : "Dr. Helen Carter");
					p.setUpdateBy(vet != null ? vet.getFullName() : "Dr. Helen Carter");
					p.setStatus("Active");
					rec4.getPrescriptions().add(p);
				}
				medicalRecordRepository.save(rec4);
			}

			// --- Lịch hẹn 5 (3 ngày trước, đã khám, đã thanh toán) ---
			Appointment app5 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("Bông") && a.getReason().equals("Kiểm tra sức khỏe định kỳ & tẩy giun")).findFirst().orElse(null);
			if (app5 == null && bong != null) {
				app5 = new Appointment();
				app5.setOwner(owner2);
				app5.setPet(bong);
				app5.setVeterinarian(vet);
				app5.setDoctor(doc1);

				app5.setAppointmentAt(LocalDateTime.now().minusDays(3));
				app5.setAppointmentTime(LocalDateTime.now().minusDays(3));
				app5.setReasonForVisit("Kiểm tra sức khỏe định kỳ & tẩy giun");
				app5.setReason("Kiểm tra sức khỏe định kỳ & tẩy giun");
				app5.setStatus(AppointmentStatus.CONFIRMED);

				app5.setPatientName(bong.getName());
				app5.setPatientPhone(owner2 != null ? owner2.getPhone() : "");
				app5.setCreatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
				app5.setUpdatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
				appointmentRepository.save(app5);

				// Hóa đơn 5 (Đã thanh toán)
				Invoice inv5 = new Invoice();
				inv5.setAppointment(app5);
				inv5.setServices(new ArrayList<>(List.of(checkupService, scanService)));
				inv5.setTotalAmount(checkupService.getPrice() + scanService.getPrice());
				inv5.setPaymentStatus(PaymentStatus.PAID);
				inv5.setCreatedAt(LocalDateTime.now().minusDays(3));
				invoiceRepository.save(inv5);

				// Bệnh án 5
				MedicalRecord rec5 = new MedicalRecord();
				rec5.setPet(bong);
				rec5.setVeterinarian(vet);
				rec5.setDoctor(doc1);
				rec5.setVisitAt(Instant.now().minus(3, ChronoUnit.DAYS));
				rec5.setStatus(MedicalRecordStatus.COMPLETED);
				rec5.setReasonForVisit("Kiểm tra sức khỏe định kỳ & tẩy giun");
				rec5.setDiagnosis("Poodle Bông khỏe mạnh bình thường, siêu âm không phát sinh bệnh lý.");
				rec5.setTreatmentNote("Siêu âm ổ bụng tổng quát. Cho uống tẩy giun Dewormer Max.");
				rec5.setFollowUpInstruction("Tẩy giun nhắc lại sau 3 tháng.");
				rec5.setNextVisitDate(LocalDate.now().plusMonths(3));
				rec5.setPatientName(bong.getName());
				rec5.setSymptoms("Không có triệu chứng bệnh.");
				rec5.setNotes("Chủ nuôi chăm sóc cún rất chu đáo.");
				rec5.setCreatedDate(LocalDateTime.now().minusDays(3));
				rec5.setCreatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
				rec5.setUpdatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
				rec5.setCreateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
				rec5.setUpdateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
				rec5.setInvoice(inv5);

				if (dewormer != null) {
					Prescription p = new Prescription();
					p.setMedicalRecord(rec5);
					p.setMedicationName(dewormer.getMedicineName());
					p.setDosage("1 viên duy nhất");
					p.setFrequency("uống sáng");
					p.setDurationDays(1);
					p.setInstructions("Uống trực tiếp hoặc trộn vào thức ăn.");
					p.setQuantity(1);
					p.setMedicine(dewormer);
					p.setDoctor(doc1);
					p.setPatientName(bong.getName());
					p.setCreatedDate(LocalDateTime.now().minusDays(3));
					p.setCreatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
					p.setUpdatedAt(Instant.now().minus(3, ChronoUnit.DAYS));
					p.setCreateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
					p.setUpdateBy(vet != null ? vet.getFullName() : "Dr. John Doe");
					p.setStatus("Active");
					rec5.getPrescriptions().add(p);
				}
				medicalRecordRepository.save(rec5);
			}

			// --- Lịch hẹn 6 (Hôm nay, đang chờ khám của Dr. Sarah Conner) ---
			Appointment app6 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("LuLu") && a.getReason().equals("Kiểm tra hậu phẫu triệt sản")).findFirst().orElse(null);
			if (app6 == null && lulu != null) {
				app6 = new Appointment();
				app6.setOwner(owner);
				app6.setPet(lulu);
				app6.setVeterinarian(vet2);
				app6.setDoctor(doc2);
				app6.setAppointmentAt(LocalDateTime.now());
				app6.setAppointmentTime(LocalDateTime.now());
				app6.setReasonForVisit("Kiểm tra hậu phẫu triệt sản");
				app6.setReason("Kiểm tra hậu phẫu triệt sản");
				app6.setStatus(AppointmentStatus.CONFIRMED);
				app6.setPatientName(lulu.getName());
				app6.setPatientPhone(owner != null ? owner.getPhone() : "");
				app6.setCreatedAt(Instant.now());
				app6.setUpdatedAt(Instant.now());
				appointmentRepository.save(app6);

				// Hóa đơn 6 (Chưa thanh toán)
				Invoice inv6 = new Invoice();
				inv6.setAppointment(app6);
				inv6.setServices(new ArrayList<>(List.of(checkupService)));
				inv6.setTotalAmount(checkupService.getPrice());
				inv6.setPaymentStatus(PaymentStatus.UNPAID);
				inv6.setCreatedAt(LocalDateTime.now());
				invoiceRepository.save(inv6);
			}

			// --- Lịch hẹn 7 (Hôm nay, đang chờ khám của Dr. John Doe) ---
			Appointment app7 = existingApps.stream().filter(a -> a.getPet() != null && a.getPet().getName().equals("MiMi") && a.getReason().equals("Tiêm vaccine nhắc lại 4 bệnh")).findFirst().orElse(null);
			if (app7 == null && mimi != null) {
				app7 = new Appointment();
				app7.setOwner(owner);
				app7.setPet(mimi);
				app7.setVeterinarian(vet);
				app7.setDoctor(doc1);
				app7.setAppointmentAt(LocalDateTime.now());
				app7.setAppointmentTime(LocalDateTime.now());
				app7.setReasonForVisit("Tiêm vaccine nhắc lại 4 bệnh");
				app7.setReason("Tiêm vaccine nhắc lại 4 bệnh");
				app7.setStatus(AppointmentStatus.CONFIRMED);
				app7.setPatientName(mimi.getName());
				app7.setPatientPhone(owner != null ? owner.getPhone() : "");
				app7.setCreatedAt(Instant.now());
				app7.setUpdatedAt(Instant.now());
				appointmentRepository.save(app7);

				// Hóa đơn 7 (Chưa thanh toán)
				Invoice inv7 = new Invoice();
				inv7.setAppointment(app7);
				inv7.setServices(new ArrayList<>(List.of(vaccineService)));
				inv7.setTotalAmount(vaccineService.getPrice());
				inv7.setPaymentStatus(PaymentStatus.UNPAID);
				inv7.setCreatedAt(LocalDateTime.now());
				invoiceRepository.save(inv7);
			}

			System.out.println("Appointments, Invoices, Records and Prescriptions seeded/updated successfully!");

			// Programmatic migration: Auto-generate invoices for any old completed appointments missing invoices
			try {
				List<Appointment> completedApps = appointmentRepository.findAll().stream()
						.filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
						.toList();
				int migratedInvoices = 0;
				for (Appointment app : completedApps) {
					boolean hasInvoice = invoiceRepository.findByAppointmentId(app.getId()).isPresent();
					if (!hasInvoice) {
						// Create a retrospective invoice
						Invoice migratedInvoice = new Invoice();
						migratedInvoice.setAppointment(app);
						migratedInvoice.setCreatedAt(app.getAppointmentTime() != null ? app.getAppointmentTime() : LocalDateTime.now());
						
						// Try to locate a matching medical record for this pet around the same day
						MedicalRecord matchingRecord = medicalRecordRepository.findAll().stream()
								.filter(mr -> mr.getPet() != null && app.getPet() != null && mr.getPet().getId().equals(app.getPet().getId()))
								.findFirst()
								.orElse(null);
						
						migratedInvoice.setMedicalRecord(matchingRecord);

						if ("PAID".equalsIgnoreCase(app.getPaymentStatus())) {
							migratedInvoice.setPaymentStatus(PaymentStatus.PAID);
						} else {
							migratedInvoice.setPaymentStatus(PaymentStatus.UNPAID);
						}

						// Calculate total amount based on associated pet services or default price
						double total = 150000.0;
						List<PetService> migratedServices = new ArrayList<>();
						petServiceRepository.findAll().stream()
								.filter(s -> s.getName().toLowerCase().contains("checkup") || s.getName().toLowerCase().contains("khám"))
								.findFirst()
								.ifPresent(migratedServices::add);
						if (migratedServices.isEmpty()) {
							petServiceRepository.findAll().stream().findFirst().ifPresent(migratedServices::add);
						}
						if (!migratedServices.isEmpty()) {
							migratedInvoice.setServices(migratedServices);
							total = migratedServices.stream().mapToDouble(PetService::getPrice).sum();
						}
						migratedInvoice.setTotalAmount(total);

						invoiceRepository.save(migratedInvoice);
						migratedInvoices++;
					}
				}
				if (migratedInvoices > 0) {
					System.out.println("Migrated " + migratedInvoices + " missing invoices for old completed appointments successfully!");
				}
			} catch (Exception e) {
				System.err.println("Error running retrospective invoice migration: " + e.getMessage());
			}
		}
	}
}
