create sequence group_id_seq
    minvalue 0;

alter sequence group_id_seq owner to postgres;

alter sequence group_id_seq owned by "group".id;

grant select, update, usage on sequence group_id_seq to anon;

grant select, update, usage on sequence group_id_seq to authenticated;

grant select, update, usage on sequence group_id_seq to service_role;

