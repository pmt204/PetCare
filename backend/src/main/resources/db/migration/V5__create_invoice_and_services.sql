CREATE TABLE pet_services (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(500),
    price double precision NOT NULL
);

CREATE TABLE invoices (
    id bigserial PRIMARY KEY,
    appointment_id bigint REFERENCES appointments(id) ON DELETE SET NULL,
    total_amount double precision NOT NULL,
    payment_status varchar(50),
    created_at timestamp
);

CREATE TABLE invoice_services (
    invoice_id bigint REFERENCES invoices(id) ON DELETE CASCADE,
    service_id bigint REFERENCES pet_services(id) ON DELETE CASCADE,
    PRIMARY KEY (invoice_id, service_id)
);
