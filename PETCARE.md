# Fullstack Development for Veterinary Clinic Management System[cite: 1]

## PROJECT OVERVIEW[cite: 1]

### 1. Project Introduction[cite: 1]

- The project simulates a veterinary clinic management system[cite: 1].
- It allows pet owners to register pet profiles, book appointments, and track medical records[cite: 1].

### 2. Backend Section (Spring Boot 4)[cite: 1]

- **Technical Requirements**: Spring Boot 4, Spring Web, Spring Security, Spring Data JPA, PostgreSQL, Flyway, Bean Validation, Swagger/OpenAPI, JUnit/MockMvc[cite: 1].
- **Technical Features**: JWT + Refresh Token authentication, RBAC authorization, pagination/filtering/sorting, centralized error handling[cite: 1].
- **Design Tasks**: Students must fully design business API groups, a normalized database schema, an API test suite with a minimum of 8 core test cases, and Swagger/OpenAPI documentation[cite: 1].
- Detailed backend requirements for each topic refer to the original document[cite: 1].

### 3. Frontend Section (React 18 + TypeScript)[cite: 1]

**Interface Description**[cite: 1]

- Pet owner interface: Manage pet profiles, book appointments, and view medical history[cite: 1].
- Veterinarian interface: Accept appointments, create medical records, and prescribe medication[cite: 1].
- Admin interface: Manage the entire clinic and invoices[cite: 1].

**Frontend Technologies**[cite: 1]

- React 18 + TypeScript, Vite, TailwindCSS, React Query, React Router v6, Axios, react-datepicker, React Hook Form[cite: 1].

**Pages and Screens to Build**[cite: 1]

- Pet profile page: List of pets, add/edit information, avatar[cite: 1].
- Appointment booking page: Select pet, veterinarian, date/time, and reason for visit[cite: 1].
- Medical history page: Medical record timeline for each pet, view details[cite: 1].
- Medical record details page: Diagnosis, prescription, PDF lab results, invoice[cite: 1].
- Veterinarian page: Daily appointments, create medical records, prescribe medication, upload lab results[cite: 1].
- Admin page: Manage veterinarians, services, invoices, and clinic workload reports[cite: 1].

**Key Components to Implement**[cite: 1]

- PetCard: Pet card with photo, name, species, breed, and last visit date[cite: 1].
- AppointmentBookingForm: Multi-step booking form (select pet → vet → date/time)[cite: 1].
- MedicalRecordTimeline: Medical record timeline by pet, sorted by visit date[cite: 1].
- PrescriptionViewer: View prescriptions in tabular format (drug name, dosage, duration)[cite: 1].
- LabResultUploader: Upload PDF lab results with filename preview[cite: 1].
- InvoiceSummary: Summary of services, medications, and total invoice for the visit[cite: 1].

**Frontend Deliverables**[cite: 1]

- Frontend source code with separated layouts by role (owner / vet / admin)[cite: 1].
- Integration with appointment, medical record, prescription, and invoice APIs[cite: 1].
- Responsive on desktop and tablet[cite: 1].
- Comprehensive frontend README[cite: 1].

---

## GENERAL TECHNICAL REQUIREMENTS[cite: 1]

### 1. Backend – Technologies and Technical Requirements[cite: 1]

- Use Spring Boot 4, Spring Web, Spring Security, Spring Data JPA, and PostgreSQL[cite: 1].
- JWT Access Token combined with Refresh Token for authentication and RBAC authorization[cite: 1].
- Bean Validation for request bodies, request params, and path variables[cite: 1].
- Standardized error responses using @ControllerAdvice and clear business error codes[cite: 1].
- API documentation using Swagger/OpenAPI; testing using JUnit, MockMvc, or Testcontainers[cite: 1].
- Flyway for migration management; MapStruct/Lombok to standardize the DTO layer[cite: 1].
- Support pagination, filtering, and sorting for list APIs[cite: 1].

### 2. Frontend – Technologies and Technical Requirements[cite: 1]

- Use React 18 + TypeScript, Vite, TailwindCSS as the UI foundation[cite: 1].
- React Router v6 for client-side routing with role-based route protection[cite: 1].
- Axios combined with interceptors to automatically attach JWTs and handle refresh tokens[cite: 1].
- React Query (TanStack Query) for server state management, caching, and invalidation[cite: 1].
- React Hook Form or equivalent for client-side form validation[cite: 1].
- Global error handling and displaying user-friendly notifications[cite: 1].
- Responsive design: fully compatible across desktop, tablet, and mobile[cite: 1].

---

## GENERAL DELIVERABLES[cite: 1]

| No. | Deliverable Component                                                                                              |
| --- | ------------------------------------------------------------------------------------------------------------------ |
| 1   | Complete backend source code with clear package structure.[cite: 1]                                                |
| 2   | Complete frontend source code with clear module structure.[cite: 1]                                                |
| 3   | ERD or database relationship diagram.[cite: 1]                                                                     |
| 4   | Swagger/OpenAPI or Postman Collection.[cite: 1]                                                                    |
| 5   | API test suite with a minimum of 8 core test cases.[cite: 1]                                                       |
| 6   | README with instructions for installation, environment variable configuration, and running both sections.[cite: 1] |

---

## FULLSTACK EVALUATION CRITERIA[cite: 1]

| Criteria                   | Description                                                                                                      | Weight       |
| -------------------------- | ---------------------------------------------------------------------------------------------------------------- | ------------ |
| Business Analysis          | Correct understanding of the problem, reasonable functional modeling, clearly defined actors and flows.[cite: 1] | 10%[cite: 1] |
| Data Design                | Normalized database schema, tight relationships, supporting business needs.[cite: 1]                             | 10%[cite: 1] |
| Backend API Quality        | Clear endpoints, reasonable requests/responses, pagination, filtering, and error handling.[cite: 1]              | 15%[cite: 1] |
| Security and Authorization | JWT, refresh token, RBAC, access restrictions matching role scope.[cite: 1]                                      | 15%[cite: 1] |
| Frontend Interface         | User-friendly UI, accurate business logic, responsive, fully integrated with APIs.[cite: 1]                      | 20%[cite: 1] |
| User Experience            | Loading states, clear error messages, client-side validation.[cite: 1]                                           | 10%[cite: 1] |
| Testing and Documentation  | API tests, Swagger/Postman, complete README with instructions to run both sections.[cite: 1]                     | 10%[cite: 1] |
| Technical Completion       | Logging, transactions, migrations, stable deployment of both backend and frontend.[cite: 1]                      | 10%[cite: 1] |
