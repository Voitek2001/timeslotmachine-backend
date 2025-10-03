create function exchange_update_trace() returns trigger
    language plpgsql
as
$$
begin
    NEW.trace_update_timestamp := current_timestamp;
    NEW.trace_flag := 'updated';
    NEW.trace_version := OLD.trace_version + 1;
    return NEW;
end;

$$;

alter function exchange_update_trace() owner to postgres;

grant execute on function exchange_update_trace() to anon;

grant execute on function exchange_update_trace() to authenticated;

grant execute on function exchange_update_trace() to service_role;

