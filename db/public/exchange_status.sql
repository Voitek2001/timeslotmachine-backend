create type exchange_status as enum ('pending', 'done', 'cancelled');

alter type exchange_status owner to postgres;

