# 🐾 Petcare

## 📖 Project Overview
Petcare is a comprehensive full-stack web application designed to simulate and manage a veterinary clinic's operations. The system streamlines the workflow between Pet Owners, Veterinarians, and Administrators, providing a seamless experience for pet profile management, appointment booking, medical record tracking, and clinic administration.

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
* **Framework:** Spring Boot 4.1.0 (Java 21)
* **Database & ORM:** PostgreSQL, Spring Data JPA
* **Security:** Spring Security, JWT (Access & Refresh Tokens), Role-Based Access Control (RBAC)
* **Database Migration:** Flyway
* **Validation & Mapping:** Bean Validation, MapStruct, Lombok
* **API Documentation:** Swagger / OpenAPI
* **Testing:** JUnit, MockMvc

### Frontend
* **Framework:** React 18, TypeScript, Vite
* **Styling:** Tailwind CSS
* **Routing & State Management:** React Router v6, React Query (TanStack Query)
* **Form & Validation:** React Hook Form
* **HTTP Client:** Axios (with interceptors for JWT)
* **UI Components:** react-datepicker

---

## ⚙️ Prerequisites
Before running the application, ensure you have the following installed:
* **Java 21** (JDK)
* **Node.js** (v18 or higher) & **npm/yarn**
* **PostgreSQL** (v14 or higher)
* **Maven** (if not using the embedded wrapper)

---

## 💻 Installation & Setup

### 1. Database Configuration
1. Open PostgreSQL and create a new database:
```sql
   CREATE DATABASE petcare_db;
