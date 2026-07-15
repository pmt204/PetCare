create table app_users (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    username varchar(100) not null unique,
    email varchar(150) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(120) not null,
    role varchar(20) not null,
    active boolean not null
);

create table pets (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    owner_id bigint not null references app_users(id),
    name varchar(100) not null,
    species varchar(30) not null,
    breed varchar(100),
    avatar_url varchar(500),
    birth_date date,
    gender varchar(50)
);

create table medical_records (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    pet_id bigint not null references pets(id),
    veterinarian_id bigint not null references app_users(id),
    visit_at timestamptz not null,
    status varchar(20) not null,
    reason_for_visit varchar(500),
    diagnosis varchar(1000) not null,
    treatment_note varchar(2000),
    follow_up_instruction varchar(1000),
    next_visit_date date
);

create index idx_medical_records_pet_id_visit_at on medical_records(pet_id, visit_at desc);

create table prescriptions (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    medical_record_id bigint not null references medical_records(id) on delete cascade,
    medication_name varchar(150) not null,
    dosage varchar(100),
    frequency varchar(100),
    duration_days integer,
    instructions varchar(1000)
);

create table lab_results (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    medical_record_id bigint not null references medical_records(id) on delete cascade,
    title varchar(200) not null,
    file_name varchar(255),
    file_url varchar(500),
    mime_type varchar(100),
    note varchar(1000)
);
