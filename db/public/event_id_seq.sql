create sequence event_id_seq
    minvalue 0;

alter sequence event_id_seq owner to postgres;

alter sequence event_id_seq owned by event.id;

grant select, update, usage on sequence event_id_seq to anon;

grant select, update, usage on sequence event_id_seq to authenticated;

grant select, update, usage on sequence event_id_seq to service_role;

