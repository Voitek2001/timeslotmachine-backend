create table instructor
(
    id        integer not null
        constraint instructor_pk
            primary key,
    full_name text    not null
);

alter table instructor
    owner to postgres;

create unique index instructor_full_name_uindex
    on instructor (full_name);

grant delete, insert, references, select, trigger, truncate, update on instructor to anon;

grant delete, insert, references, select, trigger, truncate, update on instructor to authenticated;

grant delete, insert, references, select, trigger, truncate, update on instructor to service_role;

