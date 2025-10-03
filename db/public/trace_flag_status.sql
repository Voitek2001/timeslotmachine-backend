create type trace_flag_status as enum ('active', 'deleted', 'updated');

alter type trace_flag_status owner to postgres;

