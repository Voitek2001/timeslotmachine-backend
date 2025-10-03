create table accepted_exchange
(
    exchange_acceptor_id integer not null
        constraint accepted_exchange_exchange_acceptor_id_fk
            references "user",
    exchange_id          integer not null
        constraint accepted_exchange_pk
            primary key
        constraint accepted_exchange_exchange_id_fk
            references exchange
);

alter table accepted_exchange
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on accepted_exchange to anon;

grant delete, insert, references, select, trigger, truncate, update on accepted_exchange to authenticated;

grant delete, insert, references, select, trigger, truncate, update on accepted_exchange to service_role;

