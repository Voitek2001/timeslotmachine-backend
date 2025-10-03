create sequence user_id_seq
    minvalue 0;

alter sequence user_id_seq owner to postgres;

alter sequence user_id_seq owned by "user".id;

grant select, update, usage on sequence user_id_seq to anon;

grant select, update, usage on sequence user_id_seq to authenticated;

grant select, update, usage on sequence user_id_seq to service_role;

