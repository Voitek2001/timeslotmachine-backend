create sequence auth_token_id_seq
    minvalue 0;

alter sequence auth_token_id_seq owner to postgres;

alter sequence auth_token_id_seq owned by auth_token.id;

grant select, update, usage on sequence auth_token_id_seq to anon;

grant select, update, usage on sequence auth_token_id_seq to authenticated;

grant select, update, usage on sequence auth_token_id_seq to service_role;

