create sequence exchange_id_seq;

alter sequence exchange_id_seq owner to postgres;

alter sequence exchange_id_seq owned by exchange.id;

grant select, update, usage on sequence exchange_id_seq to anon;

grant select, update, usage on sequence exchange_id_seq to authenticated;

grant select, update, usage on sequence exchange_id_seq to service_role;

