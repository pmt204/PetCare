ALTER TABLE medical_records 
ADD COLUMN doctor_id bigint REFERENCES doctors(id) ON DELETE SET NULL,
ADD COLUMN patient_name varchar(255),
ADD COLUMN created_date timestamp,
ADD COLUMN symptoms text,
ADD COLUMN notes text,
ADD COLUMN create_by varchar(255),
ADD COLUMN update_by varchar(255);

ALTER TABLE prescriptions
ADD COLUMN quantity integer,
ADD COLUMN medicine_id bigint REFERENCES medicines(id) ON DELETE SET NULL,
ADD COLUMN create_by varchar(255),
ADD COLUMN update_by varchar(255);

ALTER TABLE pets
ADD COLUMN slug varchar(150) UNIQUE,
ADD COLUMN pet_age integer;
