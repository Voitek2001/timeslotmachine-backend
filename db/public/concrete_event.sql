create table concrete_event
(
    id            integer       not null
        constraint concrete_event_pk
            primary key,
    event_id      integer       not null
        constraint concrete_event_event_id_fk
            references event,
    place_id      integer       not null
        constraint concrete_event_place_id_fk
            references place,
    user_limit    integer       not null,
    activity_form activity_form not null
);

alter table concrete_event
    owner to postgres;

create index concrete_event_event_id_index
    on concrete_event (event_id);

grant delete, insert, references, select, trigger, truncate, update on concrete_event to anon;

grant delete, insert, references, select, trigger, truncate, update on concrete_event to authenticated;

grant delete, insert, references, select, trigger, truncate, update on concrete_event to service_role;

