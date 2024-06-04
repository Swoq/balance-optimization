CREATE DATABASE balance_db;

CREATE TABLE public.users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       balance INT NOT NULL
);
--
-- -- Insert 100,000 records into the users table
-- DO $$
--     DECLARE
--         i INT;
--     BEGIN
--         FOR i IN 1..100000 LOOP
--                 INSERT INTO balance_db.users (name, balance)
--                 VALUES (md5(random()::text), (random() * 10000)::INT);
--             END LOOP;
--     END;
-- $$;

select count(*) from public.users;
