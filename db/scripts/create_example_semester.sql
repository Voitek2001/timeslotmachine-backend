DO $$
    DECLARE
        instructor_id integer := 1; -- Zastąp tym ID właściwego prowadzącego
        group1_id integer;
        algois_lecture_id integer;
        algois_lab_id integer;
        algebra_lecture_id integer;
        algebra_lab_id integer;
        analiza_lecture_id integer;
        analiza_lab_id integer;
        fizyka_lecture_id integer;
        fizyka_lab_id integer;
    BEGIN
        -- Tworzenie grupy (semestru) i zapisanie jej ID do zmiennej
        INSERT INTO "group" (id, name, description, max_points_per_concrete, status)
        VALUES (nextval('group_id_seq'), 'Grupa 1', 'Opis semestru', 100, 'idle')
        RETURNING id INTO group1_id;

        RAISE NOTICE 'Group ID: %', group1_id;

        -- Tworzenie wydarzeń (event)
        INSERT INTO event (id, group_id, name, description, color, short_name, point_limits)
        VALUES
            -- Algois wykład
            (nextval('event_id_seq'), group1_id, 'Algois', 'Algois wykład', 'FF5733', 'Algois', '{"lecture": {"minPointsPerActivity": 1}}'),
            -- Algebra wykład
            (nextval('event_id_seq'), group1_id, 'Algebra', 'Algebra wykład', '33FF57', 'Algebra', '{"lecture": {"minPointsPerActivity": 1}}'),
            -- Analiza wykład
            (nextval('event_id_seq'), group1_id, 'Analiza', 'Analiza wykład', '5733FF', 'Analiza', '{"lecture": {"minPointsPerActivity": 1}}'),
            -- Fizyka wykład
            (nextval('event_id_seq'), group1_id, 'Fizyka', 'Fizyka wykład', 'FF33A1', 'Fizyka', '{"lecture": {"minPointsPerActivity": 1}}');

        -- Tworzenie konkretnych zajęć (concrete_event) dla każdego wydarzenia
        -- Algois wykład (limit 8 miejsc, 8:00 - 9:30)
        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Algois' LIMIT 1), 0, 8, 'lecture')
        RETURNING id INTO algois_lecture_id;

        -- Algois lab (limit 4 miejsca, różne godziny)
        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Algois' LIMIT 1), 0, 4, 'classes')
        RETURNING id INTO algois_lab_id;

        -- Podobnie dla innych wydarzeń
        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Algebra' LIMIT 1), 0, 8, 'lecture')
        RETURNING id INTO algebra_lecture_id;

        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Algebra' LIMIT 1), 0, 8, 'classes')
        RETURNING id INTO algebra_lab_id;

        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Analiza' LIMIT 1), 0, 8, 'lecture')
        RETURNING id INTO analiza_lecture_id;

        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Analiza' LIMIT 1), 0, 8, 'classes')
        RETURNING id INTO analiza_lab_id;

        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Fizyka' LIMIT 1), 0, 8, 'lecture')
        RETURNING id INTO fizyka_lecture_id;

        INSERT INTO concrete_event (id, event_id, place_id, user_limit, activity_form)
        VALUES
            (nextval('concrete_event_id_seq'), (SELECT id FROM event WHERE name = 'Fizyka' LIMIT 1), 0, 8, 'classes')
        RETURNING id INTO fizyka_lab_id;

        -- Dodawanie terminów na podstawie konkretnych zajęć (concrete_event_id)

        -- Algois wykład (8:00 - 9:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), algois_lecture_id, instructor_id, '2024-10-25 08:00:00', '2024-10-25 09:30:00');

        -- Algois lab (15:00 - 16:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), algois_lab_id, instructor_id, '2024-10-25 15:00:00', '2024-10-25 16:30:00');

        -- Algebra wykład (8:00 - 9:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), algebra_lecture_id, instructor_id, '2024-10-25 08:00:00', '2024-10-25 09:30:00');

        -- Algebra lab (15:00 - 16:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), algebra_lab_id, instructor_id, '2024-10-25 15:00:00', '2024-10-25 16:30:00');

        -- Analiza wykład (8:00 - 9:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), analiza_lecture_id, instructor_id, '2024-10-25 08:00:00', '2024-10-25 09:30:00');

        -- Analiza lab (15:00 - 16:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), analiza_lab_id, instructor_id, '2024-10-25 15:00:00', '2024-10-25 16:30:00');

        -- Fizyka wykład (8:00 - 9:30)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), fizyka_lecture_id, instructor_id, '2024-10-25 08:00:00', '2024-10-25 09:30:00');

        -- Fizyka lab (16:45 - 18:15)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), fizyka_lab_id, instructor_id, '2024-10-25 16:45:00', '2024-10-25 18:15:00');

        -- Fizyka lab (18:30 - 20:00)
        INSERT INTO term (id, concrete_event_id, instructor_id, start, "end")
        VALUES
            (nextval('term_id_seq'), fizyka_lab_id, instructor_id, '2024-10-25 18:30:00', '2024-10-25 20:00:00');

    END $$;

