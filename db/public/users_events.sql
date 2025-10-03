create table users_events
(
    user_id  integer not null
        constraint users_events_user_id_fk
            references "user",
    event_id integer not null
        constraint users_events_event_id_fk
            references event,
    constraint users_events_pk
        primary key (user_id, event_id)
);

alter table users_events
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on users_events to anon;

grant delete, insert, references, select, trigger, truncate, update on users_events to authenticated;

grant delete, insert, references, select, trigger, truncate, update on users_events to service_role;

