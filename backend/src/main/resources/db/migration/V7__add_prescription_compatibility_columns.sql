ALTER TABLE prescriptions
ADD COLUMN doctor_id bigint REFERENCES doctors(id) ON DELETE SET NULL,
ADD COLUMN patient_name varchar(255),
ADD COLUMN created_date timestamp,
ADD COLUMN medicine_list text,
ADD COLUMN status varchar(50);
