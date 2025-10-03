create table "user"
(
    id            integer                   not null
        constraint user_pk
            primary key,
    username      text                      not null,
    password_hash text                      not null,
    name          text                      not null,
    surname       text                      not null,
    email         text                      not null,
    index_no      integer                   not null,
    role          role default 'user'::role not null
);

comment on column "user".password_hash is 'Note: data type can be changed to fixed-length in the future, text for debug purposes and simplification';

alter table "user"
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on "user" to anon;

grant delete, insert, references, select, trigger, truncate, update on "user" to authenticated;

grant delete, insert, references, select, trigger, truncate, update on "user" to service_role;

