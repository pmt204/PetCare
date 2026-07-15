CREATE TABLE doctors (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    specialty varchar(255) NOT NULL,
    experience_years varchar(100) NOT NULL,
    image varchar(255),
    rating double precision,
    description varchar(500) NOT NULL,
    full_description text
);

CREATE TABLE doctor_services (
    doctor_id bigint NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    service varchar(255) NOT NULL
);

CREATE TABLE medicines (
    id bigserial PRIMARY KEY,
    medicine_name varchar(255) NOT NULL,
    unit varchar(100),
    description varchar(500),
    create_at timestamp,
    update_at timestamp,
    create_by varchar(255),
    update_by varchar(255)
);

CREATE TABLE bills (
    id bigserial PRIMARY KEY,
    total_price double precision,
    create_at timestamp,
    update_at timestamp,
    create_by varchar(255),
    update_by varchar(255),
    status varchar(50),
    medical_record_id bigint REFERENCES medical_records(id) ON DELETE CASCADE
);

CREATE TABLE test_results (
    id bigserial PRIMARY KEY,
    test_name varchar(255),
    result text,
    pdf_url varchar(500),
    create_at timestamp,
    update_at timestamp,
    create_by varchar(255),
    update_by varchar(255),
    medical_record_id bigint REFERENCES medical_records(id) ON DELETE CASCADE,
    doctor_id bigint REFERENCES doctors(id) ON DELETE SET NULL,
    patient_name varchar(255),
    uploaded_date timestamp,
    test_type varchar(100),
    file_path varchar(500),
    status varchar(50)
);
