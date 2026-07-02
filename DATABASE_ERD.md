# 🐾 PetCare Database Entity-Relationship Diagram (ERD)

This document contains the ERD and relationships for the PetCare database schema.

---

## 📊 Database Schema Relationships

Below is the Mermaid diagram representing all entities and their relations.

```mermaid
erDiagram
    app_users {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        varchar username UK
        varchar email UK
        varchar password_hash
        varchar full_name
        varchar role
        boolean active
    }

    pets {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        bigint owner_id FK
        varchar name
        varchar species
        varchar breed
        varchar avatar_url
        date birth_date
        varchar gender
    }

    medical_records {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        bigint pet_id FK
        bigint veterinarian_id FK
        timestamp visit_at
        varchar status
        varchar reason_for_visit
        varchar diagnosis
        varchar treatment_note
        varchar follow_up_instruction
        date next_visit_date
    }

    prescriptions {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        bigint medical_record_id FK
        varchar medication_name
        varchar dosage
        varchar frequency
        integer duration_days
        varchar instructions
    }

    lab_results {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        bigint medical_record_id FK
        varchar title
        varchar file_name
        varchar file_url
        varchar mime_type
        varchar note
    }

    appointments {
        bigint id PK
        timestamp created_at
        timestamp updated_at
        bigint pet_id FK
        bigint veterinarian_id FK
        timestamp appointment_time
        varchar status
        varchar reason
    }

    invoices {
        bigint id PK
        bigint appointment_id FK
        bigint medical_record_id FK
        double_precision total_amount
        varchar payment_status
        timestamp created_at
    }

    pet_services {
        bigint id PK
        varchar name
        varchar description
        double_precision price
    }

    invoice_services {
        bigint invoice_id PK, FK
        bigint service_id PK, FK
    }

    refresh_tokens {
        bigint id PK
        bigint user_id FK
        varchar token UK
        timestamp expiry_date
        timestamp created_at
        timestamp updated_at
    }

    app_users ||--o{ pets : "owns"
    app_users ||--o{ medical_records : "diagnoses"
    app_users ||--o{ appointments : "schedules_with"
    app_users ||--|| refresh_tokens : "authenticates"
    pets ||--o{ medical_records : "undergoes"
    pets ||--o{ appointments : "scheduled_for"
    medical_records ||--o{ prescriptions : "prescribes"
    medical_records ||--o{ lab_results : "contains"
    medical_records ||--o| invoices : "links_to"
    appointments ||--o| invoices : "billed_by"
    invoices ||--o{ invoice_services : "includes"
    pet_services ||--o{ invoice_services : "contains"
```

---

## 🗄️ Primary Relationships

1. **User Auths**:
   - `app_users` table holds user details for all roles (OWNER, VET, ADMIN).
   - `refresh_tokens` is a 1-to-1 table containing token rotation IDs linked directly to `app_users`.
2. **Owners & Pets**:
   - One user (role `OWNER`) can own multiple `pets`. (1-to-Many).
3. **Visits & Clinic Workflows**:
   - One pet undergoes multiple `medical_records` (clinic visits).
   - One medical record can contain multiple `prescriptions` and `lab_results`.
4. **Appointments & Billing**:
   - Owners schedule `appointments` for their pets.
   - An appointment can result in an `invoice` containing multiple selected `pet_services`.
