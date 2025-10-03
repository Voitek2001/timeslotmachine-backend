create table preference
(
    user_id                     integer               not null
        constraint preference_user_id_fk
            references "user",
    concrete_event_id           integer               not null
        constraint preference_concrete_event_id_fk
            references concrete_event,
    points                      integer               not null,
    is_assigned                 boolean               not null,
    is_impossible               boolean default false not null,
    impossibility_justification text,
    constraint preference_pk
        primary key (user_id, concrete_event_id)
);

alter table preference
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on preference to anon;

grant delete, insert, references, select, trigger, truncate, update on preference to authenticated;

grant delete, insert, references, select, trigger, truncate, update on preference to service_role;

