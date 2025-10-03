create table "group"
(
    id                      integer                                     not null
        constraint group_pk
            primary key,
    name                    text                                        not null,
    description             text                                        not null,
    max_points_per_concrete integer                                     not null,
    status                  group_status default 'closed'::group_status not null
);

comment on column "group".max_points_per_concrete is 'The global limit for the whole group (semester)';

alter table "group"
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on "group" to anon;

grant delete, insert, references, select, trigger, truncate, update on "group" to authenticated;

grant delete, insert, references, select, trigger, truncate, update on "group" to service_role;

