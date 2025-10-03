create table term
(
    id                integer   not null
        constraint term_pk
            primary key,
    concrete_event_id integer   not null
        constraint term_concrete_event_id_fk
            references concrete_event,
    instructor_id     integer   not null
        constraint term_instructor_id_fk
            references instructor,
    start             timestamp not null,
    "end"             timestamp not null
);

alter table term
    owner to postgres;

create index term_concrete_event_id_index
    on term (concrete_event_id);

create index term_instructor_id_index
    on term (instructor_id);

grant delete, insert, references, select, trigger, truncate, update on term to anon;

grant delete, insert, references, select, trigger, truncate, update on term to authenticated;

grant delete, insert, references, select, trigger, truncate, update on term to service_role;

