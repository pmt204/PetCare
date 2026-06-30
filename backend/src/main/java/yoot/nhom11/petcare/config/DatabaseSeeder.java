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

		// Lấy người dùng mẫu để thiết lập quan hệ
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

		Doctor doc1 = doctorRepository.findAll().stream().filter(d -> d.getName().equals("Dr. John Doe")).findFirst().orElse(null);
		Doctor doc2 = doctorRepository.findAll().stream().filter(d -> d.getName().equals("Dr. Sarah Conner")).findFirst().orElse(null);

		// 3. Seed / Update PetServices
		{
			List<PetService> existingServices = petServiceRepository.findAll();
			
			// 1. General Checkup -> Khám bệnh tổng quát
			PetService s1 = existingServices.stream()
					.filter(s -> s.getName().equals("General Checkup") || s.getName().equals("Khám bệnh tổng quát"))
					.findFirst().orElse(new PetService());
			s1.setName("Khám bệnh tổng quát");
			s1.setDescription("Đánh giá lâm sàng tình trạng tim mạch, hệ hô hấp, tai mắt miệng và kiểm tra phản xạ vận động để phát hiện các triệu chứng bất thường.");
			s1.setPrice(150000.0);
			petServiceRepository.save(s1);

			// 2. Vaccination Package -> Tiêm phòng vaccine
			PetService s2 = existingServices.stream()
					.filter(s -> s.getName().equals("Vaccination Package") || s.getName().equals("Tiêm phòng vaccine"))
					.findFirst().orElse(new PetService());
			s2.setName("Tiêm phòng vaccine");
			s2.setDescription("Tiêm vaccine ngừa các bệnh truyền nhiễm nguy hiểm ở chó và mèo (dại, parvo, giảm bạch cầu...) kèm lập sổ theo dõi tiêm phòng định kỳ.");
			s2.setPrice(250000.0);
			petServiceRepository.save(s2);

			// 3. Surgical Sterilization -> Phẫu thuật ngoại khoa
			PetService s3 = existingServices.stream()
					.filter(s -> s.getName().equals("Surgical Sterilization") || s.getName().equals("Phẫu thuật ngoại khoa"))
					.findFirst().orElse(new PetService());
			s3.setName("Phẫu thuật ngoại khoa");
			s3.setDescription("Thực hiện các ca phẫu thuật triệt sản, mổ đẻ khẩn cấp, khâu vết thương sâu hoặc chấn thương xương khớp trong phòng mổ vô trùng đạt chuẩn.");
			s3.setPrice(1500000.0);
			petServiceRepository.save(s3);

			// 4. Blood Test & Diagnostics -> Xét nghiệm & Siêu âm
			PetService s4 = existingServices.stream()
					.filter(s -> s.getName().equals("Blood Test & Diagnostics") || s.getName().equals("Xét nghiệm & Siêu âm"))
					.findFirst().orElse(new PetService());
			s4.setName("Xét nghiệm & Siêu âm");
			s4.setDescription("Phân tích tế bào máu, xét nghiệm sinh hóa gan thận và tiến hành siêu âm ổ bụng chuyên khoa giúp chẩn đoán bệnh chính xác nhất.");
			s4.setPrice(350000.0);
			petServiceRepository.save(s4);

			// 5. Dental Cleaning -> Nha khoa thú y
			PetService s5 = existingServices.stream()
					.filter(s -> s.getName().equals("Dental Cleaning") || s.getName().equals("Nha khoa thú y"))
					.findFirst().orElse(new PetService());
			s5.setName("Nha khoa thú y");
			s5.setDescription("Lấy cao răng siêu âm an toàn, điều trị viêm nướu, nhổ răng sâu đau buốt giúp hơi thở thơm mát và cải thiện khả năng ăn nhai.");
			s5.setPrice(500000.0);
			petServiceRepository.save(s5);

			// 6. -> Điều trị nội trú theo dõi (mới)
			PetService s6 = existingServices.stream()
					.filter(s -> s.getName().equals("Điều trị nội trú theo dõi"))
					.findFirst().orElse(new PetService());
			s6.setName("Điều trị nội trú theo dõi");
			s6.setDescription("Chăm sóc đặc biệt 24/7 dành cho các trường hợp bệnh nặng, truyền dịch liên tục và theo dõi sát sao biểu hiện sinh tồn bởi bác sĩ trực.");
			s6.setPrice(400000.0);
			petServiceRepository.save(s6);

			System.out.println("Pet Services seeded/updated successfully!");
		}

		List<PetService> services = petServiceRepository.findAll();
		PetService checkupService = services.stream().filter(s -> s.getName().equals("Khám bệnh tổng quát")).findFirst().orElse(null);
		PetService vaccineService = services.stream().filter(s -> s.getName().equals("Tiêm phòng vaccine")).findFirst().orElse(null);
		PetService surgService = services.stream().filter(s -> s.getName().equals("Phẫu thuật ngoại khoa")).findFirst().orElse(null);

		// 4. Seed Pets (nếu chưa có)
		if (petRepository.count() == 0 && owner != null && owner2 != null) {
			Pet pet1 = new Pet();
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

			Pet pet2 = new Pet();
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

			Pet pet3 = new Pet();
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

			System.out.println("Pets seeded successfully!");
		}

		List<Pet> pets = petRepository.findAll();
		Pet lulu = pets.stream().filter(p -> p.getName().equals("LuLu")).findFirst().orElse(null);
		Pet mimi = pets.stream().filter(p -> p.getName().equals("MiMi")).findFirst().orElse(null);
		Pet rocky = pets.stream().filter(p -> p.getName().equals("Rocky")).findFirst().orElse(null);

		// 5. Seed Medicines (nếu chưa có)
		if (medicineRepository.count() == 0) {
			Medicine med1 = new Medicine();
			med1.setMedicineName("Amoxicillin");
			med1.setUnit("tablets");
			med1.setDescription("Antibiotic for bacterial infections in pets.");
			med1.setCreateAt(LocalDateTime.now());
			med1.setUpdateAt(LocalDateTime.now());
			med1.setCreateBy("System");
			med1.setUpdateBy("System");
			medicineRepository.save(med1);

			Medicine med2 = new Medicine();
			med2.setMedicineName("Paracetamol Vet");
			med2.setUnit("syrup");
			med2.setDescription("Pain relief and fever reducer formula for canines.");
			med2.setCreateAt(LocalDateTime.now());
			med2.setUpdateAt(LocalDateTime.now());
			med2.setCreateBy("System");
			med2.setUpdateBy("System");
			medicineRepository.save(med2);

			Medicine med3 = new Medicine();
			med3.setMedicineName("Ivermectin");
			med3.setUnit("tablets");
			med3.setDescription("Effective dewormer and parasite control.");
			med3.setCreateAt(LocalDateTime.now());
			med3.setUpdateAt(LocalDateTime.now());
			med3.setCreateBy("System");
			med3.setUpdateBy("System");
			medicineRepository.save(med3);

			System.out.println("Medicines seeded successfully!");
		}

		List<Medicine> medicines = medicineRepository.findAll();
		Medicine amox = medicines.stream().filter(m -> m.getMedicineName().equals("Amoxicillin")).findFirst().orElse(null);

		// 6. Seed Appointments & Invoices (nếu chưa có)
		if (appointmentRepository.count() == 0 && lulu != null && mimi != null && rocky != null) {
			// Lịch hẹn 1: Đã qua, trạng thái CONFIRMED (phục vụ bệnh án)
			Appointment app1 = new Appointment();
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
			app1.setPatientPhone(owner.getPhone());
			app1.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
			app1.setUpdatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
			appointmentRepository.save(app1);

			// Hóa đơn đã thanh toán cho lịch hẹn 1
			Invoice inv1 = new Invoice();
			inv1.setAppointment(app1);
			inv1.setServices(new ArrayList<>(List.of(checkupService, vaccineService)));
			inv1.setTotalAmount(checkupService.getPrice() + vaccineService.getPrice());
			inv1.setPaymentStatus(PaymentStatus.PAID);
			inv1.setCreatedAt(LocalDateTime.now().minusDays(5));
			invoiceRepository.save(inv1);

			// Lịch hẹn 2: Sắp tới, chưa thanh toán
			Appointment app2 = new Appointment();
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
			app2.setPatientPhone(owner.getPhone());
			app2.setCreatedAt(Instant.now());
			app2.setUpdatedAt(Instant.now());
			appointmentRepository.save(app2);

			// Hóa đơn chưa thanh toán cho lịch hẹn 2
			Invoice inv2 = new Invoice();
			inv2.setAppointment(app2);
			inv2.setServices(new ArrayList<>(List.of(surgService)));
			inv2.setTotalAmount(surgService.getPrice());
			inv2.setPaymentStatus(PaymentStatus.UNPAID);
			inv2.setCreatedAt(LocalDateTime.now());
			invoiceRepository.save(inv2);

			// Lịch hẹn 3: Đang yêu cầu duyệt (Pending)
			Appointment app3 = new Appointment();
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
			app3.setPatientPhone(owner2.getPhone());
			app3.setCreatedAt(Instant.now());
			app3.setUpdatedAt(Instant.now());
			appointmentRepository.save(app3);

			System.out.println("Appointments and Invoices seeded successfully!");
		}

		List<Appointment> appointments = appointmentRepository.findAll();
		Appointment completedApp = appointments.stream().filter(a -> a.getPet().getName().equals("LuLu")).findFirst().orElse(null);

		// 7. Seed MedicalRecords & Prescriptions (nếu chưa có)
		if (medicalRecordRepository.count() == 0 && completedApp != null && lulu != null) {
			MedicalRecord record = new MedicalRecord();
			record.setPet(lulu);
			record.setVeterinarian(vet);
			record.setDoctor(doc1);
			record.setVisitAt(Instant.now().minus(5, ChronoUnit.DAYS));
			record.setStatus(MedicalRecordStatus.COMPLETED);
			record.setReasonForVisit("Routine Checkup & Vaccination");
			record.setDiagnosis("Healthy Retriever, completed annual vaccines.");
			record.setTreatmentNote("Vaccinated with DHPP vaccine. Cleaned ears.");
			record.setFollowUpInstruction("Monitor injection site for swelling. Keep quiet for 24h.");
			record.setNextVisitDate(LocalDate.now().plusYears(1));
			record.setPatientName(lulu.getName());
			record.setSymptoms("Normal appetite, energetic, no coughing.");
			record.setNotes("Lulu behaves well during examination.");
			record.setCreatedDate(LocalDateTime.now().minusDays(5));
			record.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
			record.setUpdatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
			record.setCreateBy(vet.getFullName());
			record.setUpdateBy(vet.getFullName());

			// Đơn thuốc đi kèm
			if (amox != null) {
				Prescription p = new Prescription();
				p.setMedicalRecord(record);
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
				p.setCreateBy(vet.getFullName());
				p.setUpdateBy(vet.getFullName());
				p.setStatus("Active");
				record.getPrescriptions().add(p);
			}

			// Invoice thanh toán viện phí đi kèm bệnh án
			Invoice invoice = new Invoice();
			invoice.setMedicalRecord(record);
			invoice.setAppointment(completedApp);
			invoice.setTotalAmount(checkupService.getPrice() + vaccineService.getPrice() + (amox != null ? 50000.0 : 0.0));
			invoice.setCreatedAt(LocalDateTime.now().minusDays(5));
			invoice.setPaymentStatus(PaymentStatus.PAID);
			record.setInvoice(invoice);

			medicalRecordRepository.save(record);
			System.out.println("Medical Records (with Prescriptions & Bill) seeded successfully!");
		}
	}
}
