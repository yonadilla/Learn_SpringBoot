--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: restfullapi; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA restfullapi;


ALTER SCHEMA restfullapi OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: addresses; Type: TABLE; Schema: restfullapi; Owner: postgres
--

CREATE TABLE restfullapi.addresses (
    id character varying(100) NOT NULL,
    contact_id character varying(100) NOT NULL,
    street character varying(200),
    city character varying(100),
    province character varying(100),
    country character varying(100) NOT NULL,
    postal_code character varying(10)
);


ALTER TABLE restfullapi.addresses OWNER TO postgres;

--
-- Name: contacts; Type: TABLE; Schema: restfullapi; Owner: postgres
--

CREATE TABLE restfullapi.contacts (
    id character varying(100) NOT NULL,
    username character varying(100) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100),
    phone character varying(100),
    email character varying(100)
);


ALTER TABLE restfullapi.contacts OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: restfullapi; Owner: postgres
--

CREATE TABLE restfullapi.users (
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    token character varying(100),
    token_expired_at bigint
);


ALTER TABLE restfullapi.users OWNER TO postgres;

--
-- Data for Name: addresses; Type: TABLE DATA; Schema: restfullapi; Owner: postgres
--

COPY restfullapi.addresses (id, contact_id, street, city, province, country, postal_code) FROM stdin;
test-0	test	Jalan	Blora	jawaTengah	Indonesia	58381
test-1	test	Jalan	Blora	jawaTengah	Indonesia	58381
test-2	test	Jalan	Blora	jawaTengah	Indonesia	58381
test-3	test	Jalan	Blora	jawaTengah	Indonesia	58381
test-4	test	Jalan	Blora	jawaTengah	Indonesia	58381
\.


--
-- Data for Name: contacts; Type: TABLE DATA; Schema: restfullapi; Owner: postgres
--

COPY restfullapi.contacts (id, username, first_name, last_name, phone, email) FROM stdin;
test	test	Yon	Adi	123456789	yonadi123@gmail.com
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: restfullapi; Owner: postgres
--

COPY restfullapi.users (username, password, name, token, token_expired_at) FROM stdin;
test	$2a$10$XKbfHGwmVshTpIu4YMMsu.fa/UiB70M8cUYksWOEuwnsIHCx1I9iO	test	test	1704816514184
\.


--
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- Name: contacts contacts_pkey; Type: CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.contacts
    ADD CONSTRAINT contacts_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- Name: users users_token_key; Type: CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.users
    ADD CONSTRAINT users_token_key UNIQUE (token);


--
-- Name: addresses fk_contact_addresses; Type: FK CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.addresses
    ADD CONSTRAINT fk_contact_addresses FOREIGN KEY (contact_id) REFERENCES restfullapi.contacts(id);


--
-- Name: contacts fk_users_contact; Type: FK CONSTRAINT; Schema: restfullapi; Owner: postgres
--

ALTER TABLE ONLY restfullapi.contacts
    ADD CONSTRAINT fk_users_contact FOREIGN KEY (username) REFERENCES restfullapi.users(username);


--
-- PostgreSQL database dump complete
--

