create sequence instructor_id_seq
    minvalue 0;

alter sequence instructor_id_seq owner to postgres;

alter sequence instructor_id_seq owned by instructor.id;

grant select, update, usage on sequence instructor_id_seq to anon;

grant select, update, usage on sequence instructor_id_seq to authenticated;

grant select, update, usage on sequence instructor_id_seq to service_role;

