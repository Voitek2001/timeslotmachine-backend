create type group_status as enum ('closed', 'open', 'busy', 'ready', 'available', 'exchange');

alter type group_status owner to postgres;



-- rename old enum
alter type group_status rename to group_status_;
-- create a new one
create type group_status as enum ('closed', 'open', 'busy', 'ready', 'available', 'exchange');
-- rename old column
alter table "group" rename column status to status_;
-- create new column with new default value
alter table "group" add status group_status not null default 'closed';

-- copy the old column values to the new column
update "group" set status = status_::text::group_status
where status_ = 'ready';

-- drop the old column and enum
alter table "group" drop column status_;
drop type group_status_;