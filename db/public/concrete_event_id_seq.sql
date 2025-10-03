create sequence concrete_event_id_seq
    minvalue 0;

alter sequence concrete_event_id_seq owner to postgres;

alter sequence concrete_event_id_seq owned by concrete_event.id;

grant select, update, usage on sequence concrete_event_id_seq to anon;

grant select, update, usage on sequence concrete_event_id_seq to authenticated;

grant select, update, usage on sequence concrete_event_id_seq to service_role;

