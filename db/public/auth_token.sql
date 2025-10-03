create table auth_token
(
    token       uuid default gen_random_uuid() not null,
    user_id     integer                        not null
        constraint auth_token_user_id_fk
            references "user",
    expiry_date timestamp                      not null,
    id          integer                        not null
        constraint auth_token_pk
            primary key
);

alter table auth_token
    owner to postgres;

create index auth_token_token_index
    on auth_token (token);

grant delete, insert, references, select, trigger, truncate, update on auth_token to anon;

grant delete, insert, references, select, trigger, truncate, update on auth_token to authenticated;

grant delete, insert, references, select, trigger, truncate, update on auth_token to service_role;

