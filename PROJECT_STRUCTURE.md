# 📂 PetCare Project Structure Map
 
This document describes in detail the role and purpose of each directory and file in the **PetCare** project (Veterinary Clinic Management System).

---

## 🗺️ Directory Structure Overview

```text
PetCare/
├── .agents/                 # AI Agent configurations and guidelines
├── .github/                 # GitHub Workflows and upgrade scripts
├── backend/                 # Backend Source Code (Spring Boot 3.3.5, Java 21)
├── frontend/                # Frontend Source Code (React 19, TS, Vite, Tailwind CSS)
├── uploads/                 # File storage for uploads (lab results, avatars...)
├── DATABASE_ERD.md          # Database design documentation ERD (Mermaid)
├── PETCARE.md               # Project business requirements overview
├── README.md                # Installation and startup guide
└── .gitignore               # Ignored Git files configuration
```

---

## 📁 Root Directory Details

*   **[.agents](file:///C:/J2EE/ThucTap/PetCare/.agents/)**: Contains behavior rules and instructions for the AI agent when working in the project.
    *   `AGENTS.md`: Defines mandatory rules the AI must follow (e.g., asking for permission before editing files or running commands).
*   **[.github](file:///C:/J2EE/ThucTap/PetCare/.github/)**: Chứa các cấu hình CI/CD và công cụ tự động hóa.
    *   `modernize/java-upgrade/hooks/scripts/`: Chứa các script PowerShell (`recordToolUse.ps1`) và Shell script (`recordToolUse.sh`) phục vụ cho việc ghi nhận lịch sử nâng cấp/sử dụng công cụ.
*   **[uploads](file:///C:/J2EE/ThucTap/PetCare/uploads/)**: Directory for storing dynamic files uploaded from the application (e.g., PDF lab results, pet avatars).
*   **[DATABASE_ERD.md](file:///C:/J2EE/ThucTap/PetCare/DATABASE_ERD.md)**: Markdown file containing the visual database ERD (using Mermaid syntax) describing tables and their relationships.
*   **[PETCARE.md](file:///C:/J2EE/ThucTap/PetCare/PETCARE.md)**: Document containing business specifications, technology requirements, and project evaluation criteria.
*   **[README.md](file:///C:/J2EE/ThucTap/PetCare/README.md)**: Document guiding environment setup, PostgreSQL database configuration, backend & frontend startup steps, and Swagger API documentation.
*   **`.gitignore`**: List of temporary or sensitive files/folders that Git should ignore.

---

## ☕ 1. Backend (Spring Boot)

The **[backend](file:///C:/J2EE/ThucTap/PetCare/backend/)** directory contains all server-side logic, JWT security, authentication, and database access.

### ⚙️ System Configuration Files
*   `pom.xml`: Manages Maven dependency definitions (Spring Boot, Spring Security, JPA, Flyway, Lombok, JWT...).
*   `mvnw` / `mvnw.cmd`: Maven wrapper helping to build/run the project without relying on a locally installed Maven.
*   `src/main/resources/application.properties`: Configures PostgreSQL connection, JPA/Hibernate, Flyway activation, and JWT security parameters.

### 💾 Database Migration (Flyway)
Located at `src/main/resources/db/migration/`, automatically creates/updates tables on application startup:
*   `V1__init_medical_history.sql`: Initializes tables for users, roles, pet profiles, and medical records.
*   `V2__create_appointments.sql`: Initializes the appointments table.
*   `V3__create_additional_entities.sql`: Creates additional tables for bills, medicines, prescriptions, and clinic services.
*   `V4__add_appointment_compatibility_columns.sql`: Adds compatibility columns for the appointments table.
*   `V5__create_invoice_and_services.sql`: Creates upgraded invoices and services tables.
*   `V6__add_missing_audit_and_relation_columns.sql`: Adds audit columns and relationship constraints.
*   `V7__add_prescription_compatibility_columns.sql`: Updates compatibility columns for prescriptions.
*   `V8__create_refresh_token_table.sql`: Creates refresh tokens table to persist secure JWT sessions.
*   `V9__add_user_profile_columns.sql`: Adds user profile columns.
*   `V10__merge_duplicate_tables.sql`: Merges duplicate bill/invoice entities and links the `invoices` table directly to `medical_records`.
*   `V11__add_pet_weight_column.sql`: Adds pet weight column to the medical records table.
*   `V12__make_prescription_medical_record_nullable.sql`: Updates the `medical_record_id` column in the prescriptions table to drop NOT NULL constraint for independent prescribing workflows.
*   `V13__add_appointment_payment_fields.sql`: Adds columns to record the payment method and status for appointments.

### 📂 Source Code Structure (`src/main/java/yoot/nhom11/petcare/`)

#### 🔹 [PetcareApplication.java](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/PetcareApplication.java)
Main entry point of the Spring Boot application.

#### 🔹 [config/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/config/)
*   `DatabaseSeeder.java`: Automatically seeds initial data (Admin, Vet, Owner accounts, and clinic services) when the database is empty.
*   `SecurityConfig.java`: Configures Spring Security, API endpoint authorization (RBAC), CORS, JWT filter integration, and BCrypt password encoding.

#### 🔹 [security/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/security/) (JWT Security)
*   `AuthEntryPointJwt.java`: Handles unauthorized access exceptions (returns HTTP 401).
*   `AuthTokenFilter.java`: Filter checking and parsing JWT from request headers before routing to controllers.
*   `JwtUtils.java`: Utility class for generating, parsing, and validating Access and Refresh Tokens.
*   `UserDetailsImpl.java` / `UserDetailsServiceImpl.java`: Implements Spring Security UserDetails/UserDetailsService interfaces to load user data from the database.

#### 🔹 [entity/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/entity/) (JPA Entities representing Database Tables)
*   `BaseEntity.java`: Base entity class providing auditing fields (createdAt, updatedAt, createdBy, updatedBy).
*   `AppUser.java`: User account details and system roles.
*   `Doctor.java`: Detailed veterinarian profile (linked to AppUser).
*   `Pet.java`: Pet profile details (name, species, breed, avatar, owner).
*   `Appointment.java`: Appointment schedule (datetime, status, veterinarian, pet).
*   `MedicalRecord.java`: Pet medical records (diagnosis, visit reason, symptoms, notes).
*   `Prescription.java` & `Medicine.java`: Prescription and medication details.
*   `TestResult.java` & `LabResult.java`: Lab test results and accompanying outcome files.
*   `Invoice.java` & `Bill.java`: Billing invoices.
*   `PetService.java`: Clinic pet services.
*   *Enums*: `UserRole.java`, `AppointmentStatus.java`, `MedicalRecordStatus.java`, `PaymentStatus.java`, `PetSpecies.java`.

#### 🔹 [dto/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/dto/) (Data Transfer Objects)
*   `request/`: Incoming requests from the frontend (e.g. `LoginRequest.java`, `SignupRequest.java`, `AppointmentBookingRequest.java`, `PetRequest.java`, `MedicalRecordRequest.java`).
*   `response/`: Outgoing responses returned by the API (e.g. `JwtResponse.java`, `PetResponse.java`, `AppointmentResponse.java`, `MedicalRecordDetailResponse.java`).

#### 🔹 [controller/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/controller/) (REST API Controllers)
*   `AuthController.java`: Auth endpoints: sign up, log in, token refresh, log out.
*   `UserController.java`: Endpoints to manage user profile information.
*   `PetController.java`: Endpoints to manage pet records.
*   `AppointmentController.java`: Endpoints to book appointments, retrieve appointment list, and fetch busy time slots to prevent scheduling conflicts (`/api/appointments/busy-slots`).
*   `DoctorController.java`: Endpoints to view and update veterinarian information.
*   `MedicalRecordController.java`: Endpoints to view and add medical records.
*   `PrescriptionController.java`: Endpoints to manage prescriptions.
*   `TestResultController.java`: Endpoints to save clinical lab test results.
*   `FileController.java`: Endpoints to handle file uploads and download/view files.
*   `AdminInvoiceController.java`: Endpoints for administrators to manage invoices.
*   `AdminPetServiceController.java`: Endpoints for administrators to manage clinic pet services.
*   `AdminReportController.java`: Endpoints to generate revenue and veterinarian workload reports.

#### 🔹 [service/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/service/) (Service Layer - Business Logic)
*   Defines interfaces representing core services like `PetService.java`, `AppointmentService.java`, `MedicalRecordService.java`...
*   **[service/impl/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/service/impl/)**: Implements service interfaces (e.g. `PetServiceImpl.java`, `AppointmentServiceImpl.java`), serving as the core business logic hub.

#### 🔹 [repository/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/repository/) (Repository Layer - Database Queries)
*   Interfaces extending `JpaRepository` for querying database tables (e.g. `PetRepository.java`, `AppointmentRepository.java`).
*   `specification/PetSpecification.java`: Supports dynamic filtering and specifications for pets.

#### 🔹 [mapper/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/mapper/) (Mappers - Entity <-> DTO Conversion)
*   Manual mapper classes to convert data and prevent database schema exposure (e.g. `PetMapper.java`, `AppointmentMapper.java`, `MedicalRecordMapper.java`).

#### 🔹 [exception/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/exception/) (Global Exception Handling)
*   `ErrorResponse.java`: Standardized error response payload format.
*   `GlobalExceptionHandler.java`: Intercepts backend exceptions globally and returns readable HTTP status codes.

#### 🔹 [util/](file:///C:/J2EE/ThucTap/PetCare/backend/src/main/java/yoot/nhom11/petcare/util/)
*   `SlugUtils.java`: Utility helper to construct URL friendly slugs.

---

### 🧪 Test Suite
Located at `src/test/java/yoot/nhom11/petcare/`:
*   `controller/`: API controllers mocking tests using MockMvc (`AppointmentControllerTest.java`, `PetControllerTest.java`, `MedicalRecordControllerTest.java`).
*   `service/`: Unit tests verifying service business logic (`PetServiceTest.java`, `MedicalRecordServiceTest.java`, `InvoiceServiceTest.java`).
*   `mapper/`: Ensures accurate mapping between entities and DTOs (`MedicalRecordMapperTest.java`).

---

## ⚛️ 2. Frontend (React)

The **[frontend](file:///C:/J2EE/ThucTap/PetCare/frontend/)** directory contains the React SPA (Single Page Application) source code.

### ⚙️ Configuration Files
*   `package.json`: Manages npm dependencies (React 19, TypeScript, Axios, React Router, Tailwind CSS...).
*   `vite.config.ts`: Vite compiler and dev server setup.
*   `tailwind.config.js` & `postcss.config.js`: Tailwind utility CSS configurations.
*   `tsconfig.json` & TS config files: TypeScript compilation configuration.

### 📂 Source Directory Details

#### 🔹 [main.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/main.tsx) & [index.css](file:///C:/J2EE/ThucTap/PetCare/frontend/src/index.css)
*   `main.tsx`: Application entry point mapping the React root component into the HTML DOM.
*   `index.css`: Global CSS and Tailwind styles imports.

#### 🔹 [App.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/App.tsx)
Defines client-side routing using `react-router-dom`, protecting paths by role (Admin, Vet, Owner).

#### 🔹 [services/api.ts](file:///C:/J2EE/ThucTap/PetCare/frontend/src/services/api.ts)
Configures Axios instance. Integrates request/response interceptors to automatically attach Bearer JWTs and trigger refresh token rotation when expired.

#### 🔹 [context/AuthContext.tsx](file:///C:/J2EE/ThucTap/PetCare/frontend/src/context/AuthContext.tsx)
Manages global user session state (user details, accessToken, role) and auth functions (login, logout).

#### 🔹 [layouts/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/layouts/) (Layouts)
*   `DashboardLayout.tsx`: General layout containing Sidebar navigation, Header, and main viewport for Vets and Pet Owners.
*   `AdminLayout.tsx`: Dedicated layout designed for Administrator views.

#### 🔹 [components/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/components/) (Reusable Components)
*   `AppointmentBookingForm.tsx`: Step-by-step appointment booking form (Pet → Vet → Date/Time).
*   `InvoiceSummary.tsx`: Renders invoice breakdown (consultation, prescription, total fee).
*   `LabResultUploader.tsx`: Drag-and-drop file uploader component for PDF lab test results.
*   `MedicalRecordTimeline.tsx`: Timeline showing pet's medical history sorted chronologically.
*   `PetCard.tsx`: Renders a summarized pet card (photo, name, species, breed, last visit).
*   `PrescriptionViewer.tsx`: Table component displaying detailed prescription info.
*   `common/ProtectedRoute.tsx`: Route guard preventing unauthorized access based on user role.

#### 🔹 [pages/](file:///C:/J2EE/ThucTap/PetCare/frontend/src/pages/) (Pages)

##### 🏠 Public Pages
*   `Home.tsx`: Landing homepage of the clinic.
*   `About.tsx`: About us information page.
*   `Doctors.tsx`: Veterinarians listing page.
*   `Services.tsx`: Clinic medical services list.
*   `auth/Login.tsx`: Sign-in authentication page.

##### 👤 Pet Owner Pages
*   `owner/OwnerDashboard.tsx`: Owner control dashboard, displaying registered pets and upcoming schedules.
*   `owner/BookAppointment.tsx`: Appointment scheduling page.
*   `owner/MedicalRecordDetails.tsx`: Detailed view of a medical record (including diagnosis, medications, uploaded lab results, and invoices).

##### 🩺 Veterinarian Pages
*   `vet/Schedule.tsx`: Schedule tracking of doctor's daily appointments.
*   `vet/CreateRecord.tsx`: Diagnostics page for veterinarians to log visits, prescribe drugs, and upload PDF lab results.

##### 👑 Administrator Pages
*   `admin/Vets.tsx`: Veterinarians administration board (Create, Read, Update, Delete profiles).
*   `admin/Invoices.tsx`: Invoice manager to view bills and confirm payment status.
*   `admin/Reports.tsx`: Statistics page showing revenue, visit logs, and doctor workloads.
