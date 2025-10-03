create function exchange_create_trace() returns trigger
    language plpgsql
as
$$
begin
    NEW.trace_create_timestamp := current_timestamp;
    NEW.trace_update_timestamp := current_timestamp;
    NEW.trace_flag := 'active';
    NEW.trace_version := 0;
    return NEW;
end;

$$;

alter function exchange_create_trace() owner to postgres;

grant execute on function exchange_create_trace() to anon;

grant execute on function exchange_create_trace() to authenticated;

grant execute on function exchange_create_trace() to service_role;

