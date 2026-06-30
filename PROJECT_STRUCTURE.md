# 📂 Bản đồ Cấu trúc Dự án PetCare

Tài liệu này mô tả chi tiết vai trò, tác dụng của từng thư mục và tệp tin trong dự án **PetCare** (Hệ thống Quản lý Phòng khám Thú y).

---

## 🗺️ Tổng quan Cấu trúc Thư mục

```text
PetCare/
├── .agents/                 # Cấu hình và luật dành cho AI Agent
├── .github/                 # Cấu hình GitHub Workflows và Scripts nâng cấp
├── backend/                 # Mã nguồn Backend (Spring Boot 3.3.5, Java 21)
├── frontend/                # Mã nguồn Frontend (React 19, TS, Vite, Tailwind CSS)
├── uploads/                 # Thư mục lưu trữ tệp tải lên (lab results, avatar...)
├── DATABASE_ERD.md          # Tài liệu thiết kế cơ sở dữ liệu ERD (Mermaid)
├── PETCARE.md               # Mô tả tổng quan yêu cầu dự án
├── README.md                # Hướng dẫn cài đặt và chạy ứng dụng
└── .gitignore               # Cấu hình các tệp tin Git bỏ qua
```

---

## 📁 Chi tiết Thư mục Gốc (Root)

*   **[.agents](file:///C:/J2EE/ThucTap/PetCare/.agents/)**: Chứa các quy tắc hành vi và hướng dẫn riêng cho AI khi hoạt động trong dự án.
    *   `AGENTS.md`: Định nghĩa các nguyên tắc bắt buộc AI phải tuân thủ (ví dụ: xin phép trước khi sửa file/chạy lệnh).
*   **[.github](file:///C:/J2EE/ThucTap/PetCare/.github/)**: Chứa các cấu hình CI/CD và công cụ tự động hóa.
    *   `modernize/java-upgrade/hooks/scripts/`: Chứa các script PowerShell (`recordToolUse.ps1`) và Shell script (`recordToolUse.sh`) phục vụ cho việc ghi nhận lịch sử nâng cấp/sử dụng công cụ.
*   **[uploads](file:///C:/J2EE/ThucTap/PetCare/uploads/)**: Thư mục lưu trữ các file động được upload từ ứng dụng (ví dụ: kết quả xét nghiệm PDF, ảnh thú cưng).
*   **[DATABASE_ERD.md](file:///C:/J2EE/ThucTap/PetCare/DATABASE_ERD.md)**: File Markdown chứa sơ đồ ERD trực quan (sử dụng cú pháp Mermaid) mô tả các bảng cơ sở dữ liệu và quan hệ giữa chúng.
*   **[PETCARE.md](file:///C:/J2EE/ThucTap/PetCare/PETCARE.md)**: File tài liệu chứa thông tin đặc tả nghiệp vụ, yêu cầu công nghệ và tiêu chí đánh giá dự án.
*   **[README.md](file:///C:/J2EE/ThucTap/PetCare/README.md)**: Tài liệu hướng dẫn cài đặt môi trường, cấu hình cơ sở dữ liệu PostgreSQL, các bước khởi chạy Backend & Frontend, cùng tài liệu API Swagger.
*   **`.gitignore`**: Danh sách các tệp tin/thư mục tạm thời hoặc nhạy cảm mà Git không cần theo dõi.

---

## ☕ 1. Backend (Spring Boot)

Thư mục **[backend](file:///C:/J2EE/ThucTap/PetCare/backend/)** chứa toàn bộ logic xử lý phía máy chủ, bảo mật JWT, xác thực và cơ sở dữ liệu.

### ⚙️ Các file cấu hình hệ thống
*   `pom.xml`: Quản lý các dependencies của dự án Maven (Spring Boot, Spring Security, JPA, Flyway, Lombok, JWT...).
*   `mvnw` / `mvnw.cmd`: Maven wrapper giúp chạy lệnh build/run dự án không phụ thuộc vào Maven cài cục bộ.
*   `src/main/resources/application.properties`: Cấu hình kết nối PostgreSQL, JPA/Hibernate, kích hoạt Flyway và cài đặt các tham số bảo mật JWT.

### 💾 Database Migration (Flyway)
Nằm tại `src/main/resources/db/migration/`, tự động tạo/cập nhật bảng khi chạy ứng dụng:
*   `V1__init_medical_history.sql`: Khởi tạo bảng người dùng, vai trò, hồ sơ thú cưng và bệnh án.
*   `V2__create_appointments.sql`: Khởi tạo bảng đặt lịch hẹn.
*   `V3__create_additional_entities.sql`: Tạo bảng bổ sung cho hóa đơn (`bill`), thuốc (`medicine`), đơn thuốc (`prescription`), dịch vụ phòng khám (`pet_service`).
*   `V4__add_appointment_compatibility_columns.sql`: Bổ sung cột cho bảng cuộc hẹn.
*   `V5__create_invoice_and_services.sql`: Tạo bảng hóa đơn nâng cấp và dịch vụ.
*   `V6__add_missing_audit_and_relation_columns.sql`: Bổ sung các cột audit và quan hệ giữa các bảng.
*   `V7__add_prescription_compatibility_columns.sql`: Cập nhật cấu trúc đơn thuốc tương thích.
*   `V8__create_refresh_token_table.sql`: Tạo bảng lưu trữ JWT Refresh Token để duy trì đăng nhập an toàn.
*   `V9__add_user_profile_columns.sql`: Bổ sung thông tin cá nhân của người dùng.

### 📂 Cấu trúc Code Logic (`src/main/java/yoot/nhom11/petcare/`)

#### 🔹 [PetcareApplication.java](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/PetcareApplication.java)
Tệp chạy chính (Main Entry Point) của ứng dụng Spring Boot.

#### 🔹 [config/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/config/)
*   `DatabaseSeeder.java`: Tự động tạo dữ liệu mẫu (Tài khoản Admin, Vet, Owner và các dịch vụ mẫu) khi cơ sở dữ liệu trống.
*   `SecurityConfig.java`: Cấu hình Spring Security, phân quyền đường dẫn API (RBAC), CORS, tích hợp bộ lọc JWT và mã hóa mật khẩu BCrypt.

#### 🔹 [security/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/security/) (Bảo mật JWT)
*   `AuthEntryPointJwt.java`: Xử lý ngoại lệ khi người dùng không có quyền truy cập (Unauthorized - 401).
*   `AuthTokenFilter.java`: Bộ lọc (Filter) kiểm tra, giải mã JWT từ Request Header trước khi cho phép vào Controller.
*   `JwtUtils.java`: Lớp tiện ích để tạo, giải mã và xác thực tính hợp lệ của Access Token & Refresh Token.
*   `UserDetailsImpl.java` / `UserDetailsServiceImpl.java`: Hiện thực hóa Interface của Spring Security để tải thông tin người dùng từ DB.

#### 🔹 [entity/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/entity/) (Thực thể JPA biểu diễn các bảng Database)
*   `BaseEntity.java`: Lớp cha cung cấp các trường tự động (`createdAt`, `updatedAt`, `createdBy`, `updatedBy`).
*   `AppUser.java`: Thông tin tài khoản người dùng và vai trò.
*   `Doctor.java`: Thông tin chi tiết của bác sĩ thú y (liên kết với AppUser).
*   `Pet.java`: Hồ sơ thú cưng (tên, loài, giống, ảnh đại diện, chủ sở hữu).
*   `Appointment.java`: Lịch hẹn (ngày giờ, trạng thái, bác sĩ phụ trách, thú cưng).
*   `MedicalRecord.java`: Bệnh án điều trị thú cưng (chẩn đoán, lý do khám, cân nặng, nhiệt độ).
*   `Prescription.java` & `Medicine.java`: Đơn thuốc và thông tin thuốc kê cho thú cưng.
*   `TestResult.java` & `LabResult.java`: Kết quả xét nghiệm và các file kết quả đi kèm.
*   `Invoice.java` & `Bill.java`: Hóa đơn dịch vụ và thanh toán.
*   `PetService.java`: Các loại dịch vụ y tế của phòng khám.
*   *Các Enum*: `UserRole.java`, `AppointmentStatus.java`, `MedicalRecordStatus.java`, `PaymentStatus.java`, `PetSpecies.java`.

#### 🔹 [dto/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/dto/) (Dữ liệu truyền nhận giữa Client và Server)
*   `request/`: Các đối tượng dữ liệu gửi lên từ Frontend (ví dụ: `LoginRequest.java`, `SignupRequest.java`, `AppointmentBookingRequest.java`, `PetRequest.java`, `MedicalRecordRequest.java`).
*   `response/`: Các đối tượng dữ liệu trả về từ API (ví dụ: `JwtResponse.java`, `PetResponse.java`, `AppointmentResponse.java`, `MedicalRecordDetailResponse.java`).

#### 🔹 [controller/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/controller/) (Các REST API Endpoints)
*   `AuthController.java`: API Đăng ký, đăng nhập, gia hạn token (Refresh Token), đăng xuất.
*   `UserController.java`: API quản lý thông tin hồ sơ cá nhân của người dùng.
*   `PetController.java`: API CRUD thông tin thú cưng của khách hàng.
*   `AppointmentController.java`: API đặt lịch hẹn, xem danh sách hẹn.
*   `DoctorController.java`: API quản lý và lấy thông tin bác sĩ thú y.
*   `MedicalRecordController.java`: API xem và cập nhật hồ sơ bệnh án.
*   `PrescriptionController.java`: API kê đơn thuốc.
*   `TestResultController.java`: API lưu kết quả xét nghiệm lâm sàng.
*   `FileController.java`: API xử lý upload tệp tin và trả về đường dẫn tệp (file download/viewing).
*   `AdminInvoiceController.java`: API cho phép Admin quản lý hóa đơn.
*   `AdminPetServiceController.java`: API cho phép Admin quản lý các dịch vụ thú y.
*   `AdminReportController.java`: API xuất báo cáo hiệu suất làm việc và doanh thu.

#### 🔹 [service/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/service/) (Tầng chứa Logic nghiệp vụ)
*   Chứa các Interface định nghĩa các phương thức dịch vụ như `PetService.java`, `AppointmentService.java`, `MedicalRecordService.java`...
*   **[service/impl/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/service/impl/)**: Lớp hiện thực hóa các interface trên (ví dụ: `PetServiceImpl.java`, `AppointmentServiceImpl.java`). Đây là nơi xử lý logic nghiệp vụ chính.

#### 🔹 [repository/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/repository/) (Tầng truy vấn Cơ sở dữ liệu)
*   Các Interface kế thừa `JpaRepository` để thực hiện các câu lệnh SQL (ví dụ: `PetRepository.java`, `AppointmentRepository.java`).
*   `specification/PetSpecification.java`: Hỗ trợ tìm kiếm, lọc động thông tin thú cưng.

#### 🔹 [mapper/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/mapper/) (Chuyển đổi dữ liệu Entity <-> DTO)
*   Các class chuyển đổi thủ công để tránh rò rỉ cấu trúc Database ra ngoài API (ví dụ: `PetMapper.java`, `AppointmentMapper.java`, `MedicalRecordMapper.java`).

#### 🔹 [exception/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/exception/) (Xử lý lỗi tập trung)
*   `ErrorResponse.java`: Định dạng phản hồi lỗi chuẩn hóa.
*   `GlobalExceptionHandler.java`: Bắt các ngoại lệ (Exception) toàn hệ thống và trả về mã lỗi HTTP thân thiện.

#### 🔹 [util/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/util/)
*   `SlugUtils.java`: Công cụ hỗ trợ định dạng chuỗi ký tự (URL friendly).

---

### 🧪 Bộ mã nguồn Kiểm thử (Tests)
Nằm tại `src/test/java/yoot/nhom11/petcare/`:
*   `controller/`: Test giả lập API sử dụng MockMvc (`AppointmentControllerTest.java`, `PetControllerTest.java`, `MedicalRecordControllerTest.java`).
*   `service/`: Test logic của các lớp nghiệp vụ (`PetServiceTest.java`, `MedicalRecordServiceTest.java`, `InvoiceServiceTest.java`).
*   `mapper/`: Đảm bảo ánh xạ dữ liệu chính xác giữa Entity và DTO (`MedicalRecordMapperTest.java`).

---

## ⚛️ 2. Frontend (React)

Thư mục **[frontend](file:///C:/J2EE/ThucTap/PetCare/frontend/)** chứa mã nguồn giao diện người dùng SPA (Single Page Application).

### ⚙️ Các file cấu hình
*   `package.json`: Quản lý dependencies (React 19, TypeScript, Axios, React Router, Tailwind CSS...).
*   `vite.config.ts`: Cấu hình build và dev-server của Vite.
*   `tailwind.config.js` & `postcss.config.js`: Cấu hình tiện ích CSS Tailwind.
*   `tsconfig.json` & các file config TS: Cấu hình trình biên dịch TypeScript.

### 📂 Chi tiết thư mục nguồn (`src/`)

#### 🔹 [main.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/main.tsx) & [index.css](file:///C:/J2EE/ThucTap/PetCare/frontend/src/index.css)
*   `main.tsx`: Điểm khởi đầu của ứng dụng React, kết nối Component `App` vào DOM HTML.
*   `index.css`: Cấu hình CSS toàn cục và Tailwind.

#### 🔹 [App.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/App.tsx)
Định nghĩa hệ thống định tuyến (Routing) chính của ứng dụng bằng `react-router-dom`, bảo vệ các route theo vai trò (Admin, Vet, Owner).

#### 🔹 [services/api.ts](file:///C:/J2EE/ThucTap/PetCare/frontend/src/services/api.ts)
Cấu hình Axios Instance. Tích hợp Interceptors tự động đính kèm Bearer Token vào tiêu đề HTTP, đồng thời tự động gọi API làm mới token khi Access Token hết hạn.

#### 🔹 [context/AuthContext.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/context/AuthContext.tsx)
Lưu trữ trạng thái đăng nhập toàn cục của người dùng (thông tin tài khoản, accessToken, role) và các hàm `login`, `logout`.

#### 🔹 [layouts/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/layouts/) (Bố cục chung trang Web)
*   `DashboardLayout.tsx`: Bố cục thanh Sidebar điều hướng, thanh Header và nội dung chính của bảng điều khiển (dành cho Vet và Owner).
*   `AdminLayout.tsx`: Bố cục thanh điều hướng thiết kế riêng cho người quản trị.

#### 🔹 [components/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/components/) (Các Component dùng chung)
*   `AppointmentBookingForm.tsx`: Giao diện đặt lịch hẹn từng bước (Select Pet -> Select Vet -> Choose Date/Time).
*   `InvoiceSummary.tsx`: Hiển thị bảng tổng hợp hóa đơn (tiền khám, tiền thuốc, tổng cộng).
*   `LabResultUploader.tsx`: Component hỗ trợ kéo thả và tải tệp kết quả xét nghiệm định dạng PDF.
*   `MedicalRecordTimeline.tsx`: Hiển thị dòng thời gian bệnh sử của thú cưng theo thứ tự thời gian.
*   `PetCard.tsx`: Thẻ hiển thị thông tin nhanh của thú cưng (loài, giống, tuổi, chủ sở hữu).
*   `PrescriptionViewer.tsx`: Component xem đơn thuốc chi tiết.
*   `common/ProtectedRoute.tsx`: Route bảo vệ, chặn truy cập trái phép nếu không đúng vai trò tài khoản.

#### 🔹 [pages/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/pages/) (Các trang màn hình chính)

##### 🏠 Trang công cộng (Public Pages)
*   `Home.tsx`: Màn hình trang chủ giới thiệu phòng khám.
*   `About.tsx`: Giới thiệu thông tin về phòng khám.
*   `Doctors.tsx`: Hiển thị danh sách bác sĩ tại phòng khám.
*   `Services.tsx`: Danh sách các dịch vụ phòng khám cung cấp.
*   `auth/Login.tsx`: Trang đăng nhập hệ thống.

##### 👤 Trang của Khách hàng (Pet Owner Pages)
*   `owner/OwnerDashboard.tsx`: Bảng điều khiển chính của khách hàng, quản lý danh sách thú cưng của mình.
*   `owner/BookAppointment.tsx`: Trang thực hiện đặt lịch hẹn khám.
*   `owner/MedicalRecordDetails.tsx`: Xem chi tiết một bệnh án cụ thể (bao gồm chẩn đoán, thuốc, file kết quả xét nghiệm, hóa đơn).

##### 🩺 Trang của Bác sĩ thú y (Veterinarian Pages)
*   `vet/Schedule.tsx`: Quản lý danh sách các lịch hẹn khám bệnh trong ngày của bác sĩ.
*   `vet/CreateRecord.tsx`: Màn hình cho phép bác sĩ tạo bệnh án mới, chẩn đoán, kê đơn thuốc và upload tệp PDF xét nghiệm.

##### 👑 Trang của Quản trị viên (Admin Pages)
*   `admin/Vets.tsx`: Màn hình quản lý danh sách tài khoản bác sĩ thú y (Thêm, Sửa, Xóa).
*   `admin/Invoices.tsx`: Quản lý và xử lý trạng thái thanh toán hóa đơn của phòng khám.
*   `admin/Reports.tsx`: Thống kê doanh thu, số ca khám và hiệu suất làm việc của bác sĩ.
