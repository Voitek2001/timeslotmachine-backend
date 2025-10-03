create table place
(
    id           integer not null
        constraint place_pk
            primary key,
    name         text    not null,
    description  text    not null,
    localization point,
    room         text    not null
);

comment on column place.name is 'should it be unique?';

comment on column place.localization is 'Can be null if online';

alter table place
    owner to postgres;

create index place_name_index
    on place (name);

create unique index place_name_room_uindex
    on place (name, room);

grant delete, insert, references, select, trigger, truncate, update on place to anon;

grant delete, insert, references, select, trigger, truncate, update on place to authenticated;

grant delete, insert, references, select, trigger, truncate, update on place to service_role;

