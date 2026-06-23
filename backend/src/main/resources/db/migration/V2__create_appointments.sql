create table appointments (
    id bigserial primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    owner_id bigint not null references app_users(id),
    pet_id bigint not null references pets(id),
    veterinarian_id bigint not null references app_users(id),
    appointment_at timestamp not null,
    reason_for_visit varchar(1000) not null,
    status varchar(20) not null
);

create index idx_appointments_owner_id_appointment_at on appointments(owner_id, appointment_at desc);
create index idx_appointments_pet_id on appointments(pet_id);
create index idx_appointments_veterinarian_id on appointments(veterinarian_id);
