create table users_groups
(
    user_id  integer not null
        constraint users_groups_user_id_fk
            references "user",
    group_id integer not null
        constraint users_groups_group_id_fk
            references "group",
    constraint users_groups_pk
        primary key (user_id, group_id)
);

alter table users_groups
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on users_groups to anon;

grant delete, insert, references, select, trigger, truncate, update on users_groups to authenticated;

grant delete, insert, references, select, trigger, truncate, update on users_groups to service_role;

