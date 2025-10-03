create sequence place_id_seq
    minvalue 0;

alter sequence place_id_seq owner to postgres;

alter sequence place_id_seq owned by place.id;

grant select, update, usage on sequence place_id_seq to anon;

grant select, update, usage on sequence place_id_seq to authenticated;

grant select, update, usage on sequence place_id_seq to service_role;

