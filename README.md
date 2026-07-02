# 🐾 PetCare: Veterinary Clinic Management System

PetCare is a comprehensive full-stack web application designed to simulate and manage a veterinary clinic's operations. The system streamlines the workflow between Pet Owners, Veterinarians, and Administrators, providing a seamless experience for pet profile management, appointment booking, medical record tracking, and clinic administration.

---

## 🚀 Features by Role (RBAC)

### 👤 Pet Owner
* **Pet Profiles:** Register and manage pet details (name, species, breed, avatar).
* **Appointments:** Book multi-step appointments (Select Pet → Select Vet → Choose Date/Time).
* **Medical History:** View a detailed timeline of past diagnoses, prescriptions, and lab results.

### 🩺 Veterinarian
* **Schedule Management:** View and manage daily appointments.
* **Medical Records:** Create and update medical records for each visit.
* **Prescriptions:** Prescribe medications (drug name, dosage, duration).
* **Lab Results:** Upload and preview PDF lab results.

### 👑 Administrator
* **Clinic Management:** Manage veterinarian accounts and clinic services.
* **Invoicing:** Generate, summarize, and manage invoices for visits and medications.
* **Reporting:** View workload and clinic performance reports.

---

## 🛠️ Technology Stack

### Backend
* **Framework:** Spring Boot 3.3.5 (Java 21)
* **Database & ORM:** PostgreSQL, Spring Data JPA
* **Security:** Spring Security, JWT (Access & Refresh Tokens), Role-Based Access Control (RBAC)
* **Database Migration:** Flyway
* **Validation & Mapping:** Bean Validation, Lombok
* **API Documentation:** Swagger / OpenAPI
* **Testing:** JUnit, MockMvc

### Frontend
* **Framework:** React 19, TypeScript, Vite
* **Styling:** Tailwind CSS (Vanilla CSS for custom classes)
* **Routing & State Management:** React Router v6, React Query (TanStack Query)
* **Form & Validation:** React Hook Form
* **HTTP Client:** Axios (with interceptors for JWT and automatic refresh token rotation)
* **UI Components:** react-datepicker, lucide-react

---

## ⚙️ Prerequisites
Before running the application, ensure you have the following installed:
* **Java 21** (JDK)
* **Node.js** (v18 or higher) & **npm**
* **PostgreSQL** (v14 or higher)

---

## 💻 Installation & Setup

### 1. Database Configuration
1. Open PostgreSQL and create a database:
   ```sql
   CREATE DATABASE petcare_db;
   ```
2. Check `backend/src/main/resources/application.properties` and update the database URL, username, and password if they differ from your local setup:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/petcare_db
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   ```

### 2. Run the Spring Boot Backend
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build and run the project using Maven wrapper:
   ```bash
   # On Windows
   mvnw.cmd spring-boot:run
   
   # On Linux/macOS
   ./mvnw spring-boot:run
   ```
3. Flyway migrations will execute automatically and set up the schema.
4. On startup, a database seeder (`DatabaseSeeder`) will create default users if the database is empty:
   - **Admin:** `admin` / `admin123`
   - **Vet:** `vet` / `vet123`
   - **Owner:** `owner` / `owner123`

### 3. Run the React Frontend
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. Access the frontend app in your browser at `http://localhost:5173`.

---

## 📚 API Testing & Documentation

### 1. Swagger / OpenAPI Documentation
When the backend application is running, you can access the Swagger UI documentation at:
- `http://localhost:8080/swagger-ui/index.html`
This lists all available API endpoints, requests/responses structure, and security configurations.

### 2. Running Unit and Controller Tests
To run the Maven test suite containing the 8+ core mock and integration test cases:
```bash
cd backend
# Run all tests
./mvnw test
```
