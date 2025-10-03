create table event
(
    id           integer not null
        constraint event_pk
            primary key,
    group_id     integer not null
        constraint event_group_id_fk
            references "group",
    name         text    not null,
    description  text    not null,
    color        char(6) not null,
    short_name   text    not null,
    point_limits jsonb   not null
);

comment on column event.color is 'HEX value: ''#'' is omitted';

comment on column event.point_limits is 'This field allows to set minimal points limit (per activity). Format: {"classes": {"minPointsPerActivity": 1}}';

alter table event
    owner to postgres;

create index event_group_id_index
    on event (group_id);

grant delete, insert, references, select, trigger, truncate, update on event to anon;

grant delete, insert, references, select, trigger, truncate, update on event to authenticated;

grant delete, insert, references, select, trigger, truncate, update on event to service_role;

