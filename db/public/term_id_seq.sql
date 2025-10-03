create sequence term_id_seq
    minvalue 0;

alter sequence term_id_seq owner to postgres;

alter sequence term_id_seq owned by term.id;

grant select, update, usage on sequence term_id_seq to anon;

grant select, update, usage on sequence term_id_seq to authenticated;

grant select, update, usage on sequence term_id_seq to service_role;

