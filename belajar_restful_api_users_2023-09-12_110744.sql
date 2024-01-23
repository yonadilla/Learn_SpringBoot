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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: users; Type: TABLE; Schema: restfull; Owner: postgres
--

CREATE TABLE restfull.users (
    user_id integer NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    token character varying(100),
    token_expired_at bigint,
    names character varying(100)
);


ALTER TABLE restfull.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: restfull; Owner: postgres
--

CREATE SEQUENCE restfull.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restfull.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: restfull; Owner: postgres
--

ALTER SEQUENCE restfull.users_id_seq OWNED BY restfull.users.user_id;


--
-- Name: users user_id; Type: DEFAULT; Schema: restfull; Owner: postgres
--

ALTER TABLE ONLY restfull.users ALTER COLUMN user_id SET DEFAULT nextval('restfull.users_id_seq'::regclass);


--
-- Data for Name: users; Type: TABLE DATA; Schema: restfull; Owner: postgres
--

COPY restfull.users (user_id, username, password, token, token_expired_at, names) FROM stdin;
1	ggjvf	hejyu	bsimn	880	cdbqe
2	wwlsq	wwmdd	iskhs	688	wuefu
3	ntvhe	tjega	bpcee	500	bpmkj
4	fhqxn	tyanl	coabx	931	quigc
5	rbqlk	ctlyv	tvfqa	361	dmhtl
6	fpddy	orwtm	iodym	902	lrvgt
7	jvdce	oegqd	nkryr	641	kmnrf
8	qsygl	qxfgz	hwyyd	722	rcjzj
9	lqfsb	knqll	ihjig	578	jthty
10	bwfiw	metgv	pbpyr	183	kbirv
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: restfull; Owner: postgres
--

SELECT pg_catalog.setval('restfull.users_id_seq', 10, true);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: restfull; Owner: postgres
--

ALTER TABLE ONLY restfull.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: users users_token_key; Type: CONSTRAINT; Schema: restfull; Owner: postgres
--

ALTER TABLE ONLY restfull.users
    ADD CONSTRAINT users_token_key UNIQUE (token);


--
-- PostgreSQL database dump complete
--

