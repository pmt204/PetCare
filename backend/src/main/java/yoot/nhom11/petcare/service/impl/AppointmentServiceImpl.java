package yoot.nhom11.petcare.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import yoot.nhom11.petcare.dto.request.AppointmentBookingRequest;
import yoot.nhom11.petcare.dto.request.AppointmentListFilterRequest;
import yoot.nhom11.petcare.dto.request.AppointmentRequest;
import yoot.nhom11.petcare.dto.response.AppointmentOptionResponse;
import yoot.nhom11.petcare.dto.response.AppointmentResponse;
import yoot.nhom11.petcare.entity.Appointment;
import yoot.nhom11.petcare.entity.AppointmentStatus;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.entity.UserRole;
import yoot.nhom11.petcare.entity.PetService;
import yoot.nhom11.petcare.mapper.AppointmentMapper;
import yoot.nhom11.petcare.repository.AppUserRepository;
import yoot.nhom11.petcare.repository.AppointmentRepository;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.repository.PetRepository;
import yoot.nhom11.petcare.repository.PetServiceRepository;
import yoot.nhom11.petcare.service.AppointmentService;

@Service
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final PetRepository petRepository;
	private final AppUserRepository appUserRepository;
	private final DoctorRepository doctorRepository;
	private final PetServiceRepository petServiceRepository;

	@org.springframework.beans.factory.annotation.Value("${vnpay.tmn-code:DEMOV210}")
	private String vnp_TmnCode;

	@org.springframework.beans.factory.annotation.Value("${vnpay.hash-secret:RAOEXHYVSDDIIENYWSLDIIZTANXUXZFJ}")
	private String vnp_HashSecret;

	@org.springframework.beans.factory.annotation.Value("${vnpay.url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
	private String vnp_Url;

	@org.springframework.beans.factory.annotation.Value("${vnpay.return-url:http://localhost:5173/payment-callback}")
	private String vnp_ReturnUrl;

	public AppointmentServiceImpl(
			AppointmentRepository appointmentRepository,
			PetRepository petRepository,
			AppUserRepository appUserRepository,
			DoctorRepository doctorRepository,
			PetServiceRepository petServiceRepository
	) {
		this.appointmentRepository = appointmentRepository;
		this.petRepository = petRepository;
		this.appUserRepository = appUserRepository;
		this.doctorRepository = doctorRepository;
		this.petServiceRepository = petServiceRepository;
	}

	@Override
	public AppointmentOptionResponse getBookingOptions(Long ownerId) {
		List<Pet> pets = petRepository.findAllByOwnerIdOrderByNameAsc(ownerId);
		List<AppUser> veterinarians = appUserRepository.findAllByRoleAndActiveTrue(UserRole.VET);
		return AppointmentMapper.toOptionResponse(pets, veterinarians);
	}

	@Override
	@Transactional
	public AppointmentResponse createAppointment(AppointmentBookingRequest request, jakarta.servlet.http.HttpServletRequest httpServletRequest) {
		AppUser owner = appUserRepository.findById(request.ownerId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
		Pet pet = petRepository.findByIdAndOwnerId(request.petId(), owner.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
		// Pick vet: if null or 0, pick random active vet
		AppUser veterinarian;
		if (request.veterinarianId() == null || request.veterinarianId() == 0) {
			List<AppUser> allVets = appUserRepository.findAllByRoleAndActiveTrue(UserRole.VET);
			if (allVets.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No available veterinarian");
			}
			veterinarian = allVets.get(new java.util.Random().nextInt(allVets.size()));
		} else {
			veterinarian = appUserRepository.findById(request.veterinarianId())
					.filter(user -> user.getRole() == UserRole.VET && user.isActive())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinarian not found"));
		}

		Doctor doctor = doctorRepository.findAll().stream()
				.filter(d -> d.getName() != null && d.getName().equalsIgnoreCase(veterinarian.getFullName()))
				.findFirst()
				.orElse(null);

		String paymentMethod = request.paymentMethod() != null && !request.paymentMethod().isBlank() 
				? request.paymentMethod().toUpperCase() 
				: "DIRECT";
		String paymentStatus = "UNPAID";

		Appointment appointment = Appointment.builder()
				.owner(owner)
				.pet(pet)
				.veterinarian(veterinarian)
				.doctor(doctor)
				.appointmentAt(request.appointmentAt())
				.appointmentTime(request.appointmentAt()) // Sync both fields
				.reasonForVisit(request.reasonForVisit() != null && !request.reasonForVisit().isBlank() ? request.reasonForVisit().trim() : "Khám bệnh dịch vụ")
				.status(AppointmentStatus.REQUESTED)
				.paymentMethod(paymentMethod)
				.paymentStatus(paymentStatus)
				.build();

		Appointment saved = appointmentRepository.save(appointment);

		String paymentUrl = null;
		if ("VNPAY".equals(paymentMethod)) {
			double price = 100000; // default booking fee 100k VND
			if (request.serviceName() != null && !request.serviceName().isBlank()) {
				List<PetService> services = petServiceRepository.findAll();
				for (PetService ps : services) {
					if (ps.getName().equalsIgnoreCase(request.serviceName().trim())) {
						price = ps.getPrice();
						break;
					}
				}
			}

			try {
				String ipAddress = yoot.nhom11.petcare.util.VNPayUtil.getIpAddress(httpServletRequest);
				String txnRef = String.valueOf(saved.getId()) + "_" + yoot.nhom11.petcare.util.VNPayUtil.getRandomNumber(8);

				// Use Asia/Ho_Chi_Minh (UTC+7), NOT Etc/GMT+7 which is UTC-7!
				java.util.Calendar cld = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
				formatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
				String vnp_CreateDate = formatter.format(cld.getTime());
				cld.add(java.util.Calendar.MINUTE, 15);
				String vnp_ExpireDate = formatter.format(cld.getTime());

				// VNPay params - avoid special characters in vnp_OrderInfo
				java.util.Map<String, String> vnp_Params = new java.util.TreeMap<>();
				vnp_Params.put("vnp_Version", "2.1.0");
				vnp_Params.put("vnp_Command", "pay");
				vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
				vnp_Params.put("vnp_Amount", String.valueOf((long) (price * 100)));
				vnp_Params.put("vnp_CurrCode", "VND");
				vnp_Params.put("vnp_TxnRef", txnRef);
				vnp_Params.put("vnp_OrderInfo", "Thanh toan lich kham " + saved.getId());
				vnp_Params.put("vnp_OrderType", "other");
				vnp_Params.put("vnp_Locale", "vn");
				vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
				vnp_Params.put("vnp_IpAddr", ipAddress);
				vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
				vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

				// Build hash data with URL-encoded values per VNPay spec (key=urlEncode(value)&...)
				// TreeMap ensures keys are already sorted alphabetically
				StringBuilder hashData = new StringBuilder();
				StringBuilder query = new StringBuilder();
				boolean firstField = true;
				for (java.util.Map.Entry<String, String> entry : vnp_Params.entrySet()) {
					String fieldName = entry.getKey();
					String fieldValue = entry.getValue();
					if (fieldValue != null && !fieldValue.isEmpty()) {
						if (!firstField) {
							hashData.append('&');
							query.append('&');
						}
						String encodedValue = java.net.URLEncoder.encode(fieldValue, "UTF-8");
						// Hash data: key=urlEncode(value) — as VNPay official spec requires
						hashData.append(fieldName).append('=').append(encodedValue);
						// Query string: urlEncode(key)=urlEncode(value)
						query.append(java.net.URLEncoder.encode(fieldName, "UTF-8"))
							 .append('=')
							 .append(encodedValue);
						firstField = false;
					}
				}

				String vnp_SecureHash = yoot.nhom11.petcare.util.VNPayUtil.hmacSHA512(vnp_HashSecret, hashData.toString());
				String queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
				paymentUrl = vnp_Url + "?" + queryUrl;
				System.out.println("=== VNPay DEBUG ===");
				System.out.println("TmnCode: " + vnp_TmnCode);
				System.out.println("HashData: " + hashData);
				System.out.println("SecureHash: " + vnp_SecureHash);
				System.out.println("PaymentURL: " + paymentUrl);
				System.out.println("===================");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		AppointmentResponse response = AppointmentMapper.toAppointmentResponse(saved);
		response.setPaymentUrl(paymentUrl);
		return response;
	}

	@Override
	@Transactional
	public AppointmentResponse processPaymentCallback(java.util.Map<String, String> queryParams) {
		String vnp_SecureHash = queryParams.get("vnp_SecureHash");
		java.util.Map<String, String> fields = new java.util.HashMap<>();
		for (java.util.Map.Entry<String, String> entry : queryParams.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key != null && !key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
				fields.put(key, value);
			}
		}

		java.util.List<String> fieldNames = new java.util.ArrayList<>(fields.keySet());
		java.util.Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		java.util.Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = fields.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				try {
					hashData.append(fieldName);
					hashData.append('=');
					hashData.append(java.net.URLEncoder.encode(fieldValue, java.nio.charset.StandardCharsets.US_ASCII.toString()));
				} catch (Exception e) {
					// ignore
				}
				if (itr.hasNext()) {
					hashData.append('&');
				}
			}
		}

		String signValue = yoot.nhom11.petcare.util.VNPayUtil.hmacSHA512(vnp_HashSecret, hashData.toString());
		boolean isValid = signValue.equalsIgnoreCase(vnp_SecureHash);
		if (!isValid) {
			System.out.println("Warning: VNPay signature mismatch! Expected: " + signValue + ", got: " + vnp_SecureHash);
		}

		String txnRef = queryParams.get("vnp_TxnRef");
		if (txnRef == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing transaction reference");
		}

		Long appointmentId = Long.valueOf(txnRef.split("_")[0]);
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

		String responseCode = queryParams.get("vnp_ResponseCode");
		if ("00".equals(responseCode)) {
			appointment.setPaymentStatus("PAID");
			appointment.setStatus(AppointmentStatus.CONFIRMED);
		} else {
			appointment.setPaymentStatus("FAILED");
		}

		Appointment saved = appointmentRepository.save(appointment);
		return AppointmentMapper.toAppointmentResponse(saved);
	}

	@Override
	public List<AppointmentResponse> getAppointmentsByOwner(Long ownerId, AppointmentListFilterRequest request) {
		String sortProperty = resolveSortProperty(request.getSortBy());
		Sort.Direction sortDirection = request.getSortDirection() == Sort.Direction.ASC
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(sortDirection, sortProperty));

		Specification<Appointment> specification = Specification.where(hasOwnerId(ownerId));
		specification = andIfPresent(specification, hasPetId(request.getPetId()));
		specification = andIfPresent(specification, hasVeterinarianId(request.getVeterinarianId()));
		specification = andIfPresent(specification, hasStatus(request.getStatus()));
		specification = andIfPresent(specification, appointmentAtFrom(request.getFromDate()));
		specification = andIfPresent(specification, appointmentAtTo(request.getToDate()));
		specification = andIfPresent(specification, keywordContains(request.getKeyword()));

		return appointmentRepository.findAll(specification, pageable).stream()
				.map(AppointmentMapper::toAppointmentResponse)
				.toList();
	}

    @Override
    public List<AppointmentResponse> findAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NoSuchElementException("Doctor not found: " + doctorId);
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse create(yoot.nhom11.petcare.dto.request.AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new NoSuchElementException("Doctor not found: " + request.getDoctorId()));
        Appointment appointment = AppointmentMapper.toEntity(request, doctor);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse getById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
    }

    @Override
    public List<AppointmentResponse> listAll() {
        return appointmentRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")).stream()
                .map(AppointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse update(Long id, yoot.nhom11.petcare.dto.request.AppointmentRequest request) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
        AppointmentMapper.updateEntityFromRequest(request, existing);
        Appointment saved = appointmentRepository.save(existing);
        return AppointmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) throw new NoSuchElementException("Appointment not found: " + id);
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found: " + id));
        appointment.setStatus(yoot.nhom11.petcare.entity.AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentMapper.toResponse(saved);
    }

	private String resolveSortProperty(String sortBy) {
		if (sortBy == null || sortBy.isBlank()) {
			return "appointmentAt";
		}

		String normalized = sortBy.trim();
		return switch (normalized) {
			case "appointmentAt", "createdAt", "status" -> normalized;
			default -> "appointmentAt";
		};
	}

	private Specification<Appointment> hasOwnerId(Long ownerId) {
		return (root, query, builder) -> builder.equal(root.get("owner").get("id"), ownerId);
	}

	private Specification<Appointment> hasPetId(Long petId) {
		if (petId == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("pet").get("id"), petId);
	}

	private Specification<Appointment> hasVeterinarianId(Long veterinarianId) {
		if (veterinarianId == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("veterinarian").get("id"), veterinarianId);
	}

	private Specification<Appointment> hasStatus(AppointmentStatus status) {
		if (status == null) {
			return null;
		}
		return (root, query, builder) -> builder.equal(root.get("status"), status);
	}

	private Specification<Appointment> appointmentAtFrom(LocalDate fromDate) {
		if (fromDate == null) {
			return null;
		}

		LocalDateTime fromDateTime = fromDate.atStartOfDay();
		return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("appointmentAt"), fromDateTime);
	}

	private Specification<Appointment> appointmentAtTo(LocalDate toDate) {
		if (toDate == null) {
			return null;
		}

		LocalDateTime toDateTime = toDate.plusDays(1).atStartOfDay().minusNanos(1);
		return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("appointmentAt"), toDateTime);
	}

	private Specification<Appointment> keywordContains(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return null;
		}

		String like = "%" + keyword.trim().toLowerCase() + "%";
		return (root, query, builder) -> builder.like(builder.lower(root.get("reasonForVisit")), like);
	}

	private Specification<Appointment> andIfPresent(Specification<Appointment> base, Specification<Appointment> extra) {
		return extra == null ? base : base.and(extra);
	}

	@Override
	public List<String> getBusySlots(Long vetId, String dateStr) {
		if (dateStr == null || dateStr.isBlank()) {
			return List.of();
		}
		
		try {
			LocalDate date = LocalDate.parse(dateStr);
			LocalDateTime startOfDay = date.atStartOfDay();
			LocalDateTime endOfDay = date.atTime(java.time.LocalTime.MAX);

			java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

			if (vetId != null) {
				AppUser vet = appUserRepository.findById(vetId).orElse(null);
				if (vet != null) {
					List<Appointment> appointments = appointmentRepository.findAll().stream()
							.filter(a -> a.getAppointmentTime() != null 
									&& !a.getAppointmentTime().isBefore(startOfDay) 
									&& !a.getAppointmentTime().isAfter(endOfDay)
									&& ((a.getVeterinarian() != null && a.getVeterinarian().getId().equals(vetId)) 
										|| (a.getDoctor() != null && vet.getFullName() != null && a.getDoctor().getName() != null && a.getDoctor().getName().equalsIgnoreCase(vet.getFullName()))))
							.filter(a -> a.getStatus() != yoot.nhom11.petcare.entity.AppointmentStatus.CANCELLED)
							.toList();
					return appointments.stream()
							.map(a -> a.getAppointmentTime().format(timeFormatter))
							.distinct()
							.toList();
				} else {
					return List.of();
				}
			} else {
				// VetId is null (Bác sĩ ngẫu nhiên). Check if all active doctors are fully booked at each slot.
				List<AppUser> activeVets = appUserRepository.findAllByRoleAndActiveTrue(yoot.nhom11.petcare.entity.UserRole.VET);
				int totalActiveVets = activeVets.isEmpty() ? 1 : activeVets.size();

				List<Appointment> allDayApps = appointmentRepository.findAll().stream()
						.filter(a -> a.getAppointmentTime() != null 
								&& !a.getAppointmentTime().isBefore(startOfDay) 
								&& !a.getAppointmentTime().isAfter(endOfDay))
						.filter(a -> a.getStatus() != yoot.nhom11.petcare.entity.AppointmentStatus.CANCELLED)
						.toList();

				java.util.Map<String, Long> slotCounts = allDayApps.stream()
						.collect(java.util.stream.Collectors.groupingBy(
								a -> a.getAppointmentTime().format(timeFormatter),
								java.util.stream.Collectors.counting()
						));

				return slotCounts.entrySet().stream()
						.filter(entry -> entry.getValue() >= totalActiveVets)
						.map(java.util.Map.Entry::getKey)
						.toList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}
}
