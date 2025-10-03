create function exchange_delete_trace() returns trigger
    language plpgsql
as
$$
begin
    NEW.trace_delete_timestamp := current_timestamp;
    NEW.trace_flag := 'deleted';
return NEW;
end;

$$;

alter function exchange_delete_trace() owner to postgres;

grant execute on function exchange_delete_trace() to anon;

grant execute on function exchange_delete_trace() to authenticated;

grant execute on function exchange_delete_trace() to service_role;

