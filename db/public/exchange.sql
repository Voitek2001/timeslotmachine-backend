create table exchange
(
    id                     integer                                               not null
        constraint exchange_pk
            primary key,
    exchange_initiator_id  integer                                               not null
        constraint exchange_user_id_fk
            references "user",
    con_event_offered_id   integer                                               not null
        constraint exchange_con_event_offered_id_fk
            references concrete_event,
    con_event_requested_id integer                                               not null
        constraint exchange_con_event_requested_id_fk
            references concrete_event,
    status                 exchange_status   default 'pending'::exchange_status  not null,
    exchange_identifier    uuid                                                  not null,
    is_private             boolean           default false                       not null,
    trace_create_timestamp timestamp                                             not null,
    trace_update_timestamp timestamp                                             not null,
    trace_delete_timestamp timestamp,
    trace_version          integer                                               not null,
    trace_flag             trace_flag_status default 'active'::trace_flag_status not null
);

alter table exchange
    owner to postgres;

create trigger exchange_delete_trace_trigger
    before delete
    on exchange
    for each row
execute procedure exchange_delete_trace();

create trigger exchange_update_trace_trigger
    before update
    on exchange
    for each row
execute procedure exchange_update_trace();

create trigger exchange_create_trace_trigger
    before insert
    on exchange
    for each row
execute procedure exchange_create_trace();

grant delete, insert, references, select, trigger, truncate, update on exchange to anon;

grant delete, insert, references, select, trigger, truncate, update on exchange to authenticated;

grant delete, insert, references, select, trigger, truncate, update on exchange to service_role;

