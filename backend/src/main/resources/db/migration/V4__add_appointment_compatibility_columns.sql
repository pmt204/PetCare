ALTER TABLE appointments 
ADD COLUMN doctor_id bigint REFERENCES doctors(id) ON DELETE SET NULL,
ADD COLUMN patient_name varchar(255),
ADD COLUMN patient_phone varchar(50),
ADD COLUMN appointment_time timestamp,
ADD COLUMN reason varchar(1000);
