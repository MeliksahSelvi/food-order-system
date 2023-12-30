DROP SCHEMA IF EXISTS user CASCADE;

CREATE SCHEMA user;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" with schema user ;

CREATE TABLE "user".users
(
    id uuid NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
