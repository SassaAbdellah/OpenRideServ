--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.waypoint DROP CONSTRAINT waypoint_ride_id_fkey;
ALTER TABLE ONLY public.route_point DROP CONSTRAINT route_point_ride_id_fkey;
ALTER TABLE ONLY public.match DROP CONSTRAINT riderrouteid;
ALTER TABLE ONLY public.match DROP CONSTRAINT rideid;
ALTER TABLE ONLY public.registration_pass DROP CONSTRAINT registration_pass_custid_fkey;
ALTER TABLE ONLY public.riderundertakesride DROP CONSTRAINT fk_riderundertakesride_ride_id;
ALTER TABLE ONLY public.riderundertakesride DROP CONSTRAINT fk_riderundertakesride_cust_id;
ALTER TABLE ONLY public.favoritepoint DROP CONSTRAINT fk_favoritepoints_cust_id;
ALTER TABLE ONLY public.driverundertakesride DROP CONSTRAINT fk_driverundertakesride_cust_id;
ALTER TABLE ONLY public.cardetails DROP CONSTRAINT fk_cardetails_cust_id;
ALTER TABLE ONLY public.accounthistory DROP CONSTRAINT fk_accounthistory_cust_id;
ALTER TABLE ONLY public.drive_route_point DROP CONSTRAINT drive_route_point_fk;
DROP TRIGGER riderundertakesride_trigger ON public.riderundertakesride;
DROP TRIGGER drive_route_point_trigger ON public.drive_route_point;
ALTER TABLE ONLY public.waypoint DROP CONSTRAINT waypoint_pkey;
ALTER TABLE ONLY public.favoritepoint DROP CONSTRAINT uc_favoritepoints_cust_id_displayname;
ALTER TABLE ONLY public.sequence DROP CONSTRAINT sequence_pkey;
ALTER TABLE ONLY public.route_point DROP CONSTRAINT route_point_pkey;
ALTER TABLE ONLY public.riderundertakesride DROP CONSTRAINT riderundertakesride_pkey;
ALTER TABLE ONLY public.registration_pass DROP CONSTRAINT registration_pass_passcode_key;
ALTER TABLE ONLY public.customer DROP CONSTRAINT nickname_unique;
ALTER TABLE ONLY public.match DROP CONSTRAINT match_pkey;
ALTER TABLE ONLY public.favoritepoint DROP CONSTRAINT favoritepoints_pkey;
ALTER TABLE ONLY public.driverundertakesride DROP CONSTRAINT driverundertakesride_pkey;
ALTER TABLE ONLY public.drive_route_point DROP CONSTRAINT drive_route_point_pk;
ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
ALTER TABLE ONLY public.cardetails DROP CONSTRAINT cardetails_pkey;
ALTER TABLE ONLY public.accounthistory DROP CONSTRAINT accounthistory_pkey;
ALTER TABLE ONLY public.registration_pass DROP CONSTRAINT "RegsitrationPass_pkey";
ALTER TABLE public.registration_pass ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.favoritepoint ALTER COLUMN favpt_id DROP DEFAULT;
ALTER TABLE public.cardetails ALTER COLUMN cardet_id DROP DEFAULT;
DROP TABLE public.waypoint;
DROP TABLE public.sequence;
DROP TABLE public.route_point;
DROP TABLE public.riderundertakesride;
DROP SEQUENCE public.registration_pass_id_seq;
DROP TABLE public.registration_pass;
DROP TABLE public.match;
DROP SEQUENCE public.favoritepoints_favpt_id_seq;
DROP TABLE public.favoritepoint;
DROP TABLE public.driverundertakesride;
DROP TABLE public.drive_route_point;
DROP TABLE public.customer;
DROP SEQUENCE public.cardetails_cardet_id_seq;
DROP TABLE public.cardetails;
DROP TABLE public.accounthistory;
DROP FUNCTION public.st_asgml(version integer, geog geography, maxdecimaldigits integer, options integer, nprefix text);
DROP FUNCTION public.st_asgml(version integer, geom geometry, maxdecimaldigits integer, options integer, nprefix text);
DROP FUNCTION public.adjustgeomriderundertakesride();
DROP FUNCTION public.adjustgeomdriveroutepoint();
DROP FUNCTION public._st_asgml(integer, geography, integer, integer, text);
DROP FUNCTION public._st_asgml(integer, geometry, integer, integer, text);
DROP EXTENSION postgis;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- Name: openride; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE openride IS 'Database for openride ridesharing app';


--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


SET search_path = public, pg_catalog;

--
-- Name: _st_asgml(integer, geometry, integer, integer, text); Type: FUNCTION; Schema: public; Owner: openride
--

CREATE FUNCTION _st_asgml(integer, geometry, integer, integer, text) RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.1', 'LWGEOM_asGML';


ALTER FUNCTION public._st_asgml(integer, geometry, integer, integer, text) OWNER TO openride;

--
-- Name: _st_asgml(integer, geography, integer, integer, text); Type: FUNCTION; Schema: public; Owner: openride
--

CREATE FUNCTION _st_asgml(integer, geography, integer, integer, text) RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.1', 'geography_as_gml';


ALTER FUNCTION public._st_asgml(integer, geography, integer, integer, text) OWNER TO openride;

--
-- Name: adjustgeomdriveroutepoint(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION adjustgeomdriveroutepoint() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN
    NEW.coordinate_c = st_transform(st_setSrid(st_makepoint(NEW.coordinate[0], NEW.coordinate[1]), 4326), 3068);   
    RETURN NEW;
  END;
$$;


ALTER FUNCTION public.adjustgeomdriveroutepoint() OWNER TO postgres;

--
-- Name: adjustgeomriderundertakesride(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION adjustgeomriderundertakesride() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN
    NEW.startpt_c = st_transform(st_setSrid(st_makepoint(NEW.startpt[0], NEW.startpt[1]), 4326), 3068);   
    NEW.endpt_c = st_transform(st_setSrid(st_makepoint(NEW.endpt[0], NEW.endpt[1]), 4326), 3068);   
    RETURN NEW;
  END;
$$;


ALTER FUNCTION public.adjustgeomriderundertakesride() OWNER TO postgres;

--
-- Name: st_asgml(integer, geometry, integer, integer, text); Type: FUNCTION; Schema: public; Owner: openride
--

CREATE FUNCTION st_asgml(version integer, geom geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0, nprefix text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $_$ SELECT _ST_AsGML($1, $2, $3, $4,$5); $_$;


ALTER FUNCTION public.st_asgml(version integer, geom geometry, maxdecimaldigits integer, options integer, nprefix text) OWNER TO openride;

--
-- Name: FUNCTION st_asgml(version integer, geom geometry, maxdecimaldigits integer, options integer, nprefix text); Type: COMMENT; Schema: public; Owner: openride
--

COMMENT ON FUNCTION st_asgml(version integer, geom geometry, maxdecimaldigits integer, options integer, nprefix text) IS 'args: version, geom, maxdecimaldigits=15, options=0, nprefix=null - Return the geometry as a GML version 2 or 3 element.';


--
-- Name: st_asgml(integer, geography, integer, integer, text); Type: FUNCTION; Schema: public; Owner: openride
--

CREATE FUNCTION st_asgml(version integer, geog geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0, nprefix text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $_$ SELECT _ST_AsGML($1, $2, $3, $4, $5);$_$;


ALTER FUNCTION public.st_asgml(version integer, geog geography, maxdecimaldigits integer, options integer, nprefix text) OWNER TO openride;

--
-- Name: FUNCTION st_asgml(version integer, geog geography, maxdecimaldigits integer, options integer, nprefix text); Type: COMMENT; Schema: public; Owner: openride
--

COMMENT ON FUNCTION st_asgml(version integer, geog geography, maxdecimaldigits integer, options integer, nprefix text) IS 'args: version, geog, maxdecimaldigits=15, options=0, nprefix=null - Return the geometry as a GML version 2 or 3 element.';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: accounthistory; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE accounthistory (
    account_amount double precision,
    account_action character varying(255),
    cust_id integer NOT NULL,
    account_timestamp timestamp without time zone NOT NULL
);


ALTER TABLE public.accounthistory OWNER TO openride;

--
-- Name: cardetails; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE cardetails (
    cardet_id integer NOT NULL,
    cust_id integer NOT NULL,
    cardet_colour character varying,
    cardet_brand character varying,
    cardet_buildyear smallint,
    cardet_plateno character varying
);


ALTER TABLE public.cardetails OWNER TO openride;

--
-- Name: cardetails_cardet_id_seq; Type: SEQUENCE; Schema: public; Owner: openride
--

CREATE SEQUENCE cardetails_cardet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cardetails_cardet_id_seq OWNER TO openride;

--
-- Name: cardetails_cardet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: openride
--

ALTER SEQUENCE cardetails_cardet_id_seq OWNED BY cardetails.cardet_id;


--
-- Name: customer; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE customer (
    cust_id integer NOT NULL,
    cust_addr_zipcode character varying(255),
    cust_addr_city character varying(255),
    cust_nickname character varying(255),
    cust_driverpref_age integer,
    cust_firstname character varying(255),
    cust_driverpref_gender character(1),
    cust_dateofbirth date,
    cust_driverpref_musictaste character varying(255),
    cust_mobilephoneno character varying(255),
    cust_riderpref_age integer,
    cust_email character varying(255),
    cust_riderpref_gender character(1),
    cust_issmoker boolean,
    cust_riderpref_musictaste character varying(255),
    cust_postident boolean,
    cust_bank_account integer,
    cust_bank_code integer,
    cust_lastname character varying(255),
    cust_presencemssg character varying(255),
    cust_fixedphoneno character varying(255),
    cust_registrdate time without time zone,
    cust_licensedate date,
    cust_account_balance double precision,
    cust_passwd character varying(255),
    cust_profilepic character varying(255),
    cust_gender character(1),
    cust_addr_street character varying(255),
    cust_driverpref_smoker character(1),
    cust_riderpref_smoker character(1),
    cust_session_id integer,
    is_logged_in boolean,
    cust_group character varying(255),
    last_customer_check timestamp without time zone,
    last_matching_change timestamp without time zone
);


ALTER TABLE public.customer OWNER TO openride;

--
-- Name: drive_route_point; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE drive_route_point (
    drive_id integer NOT NULL,
    route_idx integer NOT NULL,
    coordinate point NOT NULL,
    expected_arrival timestamp without time zone NOT NULL,
    seats_available integer NOT NULL,
    coordinate_c geometry,
    distance_to_source double precision,
    CONSTRAINT enforce_dims_coordinate_c CHECK ((st_ndims(coordinate_c) = 2)),
    CONSTRAINT enforce_geotype_coordinate_c CHECK (((geometrytype(coordinate_c) = 'POINT'::text) OR (coordinate_c IS NULL))),
    CONSTRAINT enforce_srid_coordinate_c CHECK ((st_srid(coordinate_c) = 3068))
);


ALTER TABLE public.drive_route_point OWNER TO openride;

--
-- Name: driverundertakesride; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE driverundertakesride (
    ride_id integer NOT NULL,
    ride_weekdays character varying(255),
    ride_series_id integer,
    ride_starttime timestamp without time zone,
    ride_comment character varying(255),
    ride_acceptable_detour_in_min integer,
    ride_offeredseats_no integer,
    cust_id integer,
    ride_startpt point,
    ride_endpt point,
    ride_currpos point,
    startpt_addr character varying(255),
    endpt_addr character varying(255),
    ride_acceptable_detour_in_km integer,
    ride_acceptable_detour_in_percent integer,
    ride_route_point_distance_meters double precision NOT NULL,
    last_matching_state integer,
    is_countermanded boolean
);


ALTER TABLE public.driverundertakesride OWNER TO openride;

--
-- Name: favoritepoint; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE favoritepoint (
    favpt_point character varying(255) NOT NULL,
    cust_id integer NOT NULL,
    favpt_frequency integer NOT NULL,
    favpt_displayname character varying(255),
    favpt_address character varying(255),
    favpt_id integer NOT NULL
);


ALTER TABLE public.favoritepoint OWNER TO openride;

--
-- Name: favoritepoints_favpt_id_seq; Type: SEQUENCE; Schema: public; Owner: openride
--

CREATE SEQUENCE favoritepoints_favpt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.favoritepoints_favpt_id_seq OWNER TO openride;

--
-- Name: favoritepoints_favpt_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: openride
--

ALTER SEQUENCE favoritepoints_favpt_id_seq OWNED BY favoritepoint.favpt_id;


--
-- Name: match; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE match (
    riderroute_id integer NOT NULL,
    ride_id integer NOT NULL,
    match_shared_distance_meters double precision,
    match_detour_meters double precision,
    match_expected_start_time timestamp without time zone,
    match_drive_remaining_distance_meters double precision,
    match_price_cents integer,
    driver_change timestamp without time zone,
    rider_change timestamp without time zone,
    driver_access timestamp without time zone,
    rider_access timestamp without time zone,
    driver_state integer,
    rider_state integer,
    rider_message character varying(255),
    driver_message character varying(255)
);


ALTER TABLE public.match OWNER TO openride;

--
-- Name: registration_pass; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE registration_pass (
    id integer NOT NULL,
    passcode character varying(32) NOT NULL,
    creation_date date DEFAULT ('now'::text)::date NOT NULL,
    usage_date date,
    cust_id integer
);


ALTER TABLE public.registration_pass OWNER TO openride;

--
-- Name: registration_pass_id_seq; Type: SEQUENCE; Schema: public; Owner: openride
--

CREATE SEQUENCE registration_pass_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.registration_pass_id_seq OWNER TO openride;

--
-- Name: registration_pass_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: openride
--

ALTER SEQUENCE registration_pass_id_seq OWNED BY registration_pass.id;


--
-- Name: riderundertakesride; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE riderundertakesride (
    riderroute_id integer NOT NULL,
    ride_id integer,
    starttime_latest timestamp without time zone,
    timestamprealized timestamp without time zone,
    price double precision,
    cust_id integer,
    no_passengers integer,
    timestampbooked timestamp without time zone,
    account_timestamp timestamp without time zone,
    starttime_earliest timestamp without time zone,
    startpt point,
    endpt point,
    startpt_addr character varying(255),
    endpt_addr character varying(255),
    givenrating smallint,
    givenrating_comment character varying(80),
    givenrating_date timestamp without time zone,
    receivedrating smallint,
    receivedrating_comment character varying(80),
    receivedrating_date timestamp without time zone,
    startpt_c geometry,
    endpt_c geometry,
    comment character varying(255),
    last_matching_state integer,
    is_countermanded boolean,
    CONSTRAINT enforce_dims_endpt_c CHECK ((st_ndims(endpt_c) = 2)),
    CONSTRAINT enforce_dims_startpt_c CHECK ((st_ndims(startpt_c) = 2)),
    CONSTRAINT enforce_geotype_endpt_c CHECK (((geometrytype(endpt_c) = 'POINT'::text) OR (endpt_c IS NULL))),
    CONSTRAINT enforce_geotype_startpt_c CHECK (((geometrytype(startpt_c) = 'POINT'::text) OR (startpt_c IS NULL))),
    CONSTRAINT enforce_srid_endpt_c CHECK ((st_srid(endpt_c) = 3068)),
    CONSTRAINT enforce_srid_startpt_c CHECK ((st_srid(startpt_c) = 3068))
);


ALTER TABLE public.riderundertakesride OWNER TO openride;

--
-- Name: route_point; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE route_point (
    ride_id integer NOT NULL,
    route_idx integer NOT NULL,
    longitude double precision NOT NULL,
    latitude double precision NOT NULL,
    riderroute_id integer,
    is_required boolean NOT NULL
);


ALTER TABLE public.route_point OWNER TO openride;

--
-- Name: sequence; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE sequence (
    seq_name character varying(50) NOT NULL,
    seq_count numeric(38,0)
);


ALTER TABLE public.sequence OWNER TO openride;

--
-- Name: waypoint; Type: TABLE; Schema: public; Owner: openride; Tablespace: 
--

CREATE TABLE waypoint (
    waypoint_id integer NOT NULL,
    ride_id integer NOT NULL,
    route_idx integer NOT NULL,
    longitude double precision NOT NULL,
    latitude double precision NOT NULL,
    description text
);


ALTER TABLE public.waypoint OWNER TO openride;

--
-- Name: cardet_id; Type: DEFAULT; Schema: public; Owner: openride
--

ALTER TABLE ONLY cardetails ALTER COLUMN cardet_id SET DEFAULT nextval('cardetails_cardet_id_seq'::regclass);


--
-- Name: favpt_id; Type: DEFAULT; Schema: public; Owner: openride
--

ALTER TABLE ONLY favoritepoint ALTER COLUMN favpt_id SET DEFAULT nextval('favoritepoints_favpt_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: openride
--

ALTER TABLE ONLY registration_pass ALTER COLUMN id SET DEFAULT nextval('registration_pass_id_seq'::regclass);


--
-- Data for Name: accounthistory; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY accounthistory (account_amount, account_action, cust_id, account_timestamp) FROM stdin;
\.


--
-- Data for Name: cardetails; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY cardetails (cardet_id, cust_id, cardet_colour, cardet_brand, cardet_buildyear, cardet_plateno) FROM stdin;
35301	24852	British Racing Green	Lotus Seven	1958	BAS-HH
\.


--
-- Name: cardetails_cardet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: openride
--

SELECT pg_catalog.setval('cardetails_cardet_id_seq', 1, false);


--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY customer (cust_id, cust_addr_zipcode, cust_addr_city, cust_nickname, cust_driverpref_age, cust_firstname, cust_driverpref_gender, cust_dateofbirth, cust_driverpref_musictaste, cust_mobilephoneno, cust_riderpref_age, cust_email, cust_riderpref_gender, cust_issmoker, cust_riderpref_musictaste, cust_postident, cust_bank_account, cust_bank_code, cust_lastname, cust_presencemssg, cust_fixedphoneno, cust_registrdate, cust_licensedate, cust_account_balance, cust_passwd, cust_profilepic, cust_gender, cust_addr_street, cust_driverpref_smoker, cust_riderpref_smoker, cust_session_id, is_logged_in, cust_group, last_customer_check, last_matching_change) FROM stdin;
24853	\N	\N	user2	\N	user2	\N	\N	\N	\N	\N	user2	\N	\N	\N	\N	\N	\N	user2	\N	\N	09:11:21	\N	\N	7e58d63b60197ceb55a1c487989a3720	\N	-	\N	\N	\N	0	\N	customer	2014-05-01 13:41:15.739	2014-05-04 21:24:52.832
24852	\N	\N	user1	\N	user1	\N	\N	\N	\N	\N	user1	\N	\N	\N	\N	\N	\N	user1	\N	\N	09:11:01	\N	\N	24c9e15e52afc47c225b757e7bee1f9d	\N	-	\N	\N	\N	0	\N	customer	2014-05-04 21:48:39.193	2014-05-04 21:24:52.832
56302	\N	\N	userXXX	\N	user1	\N	\N	\N	\N	\N	userXXX	\N	\N	\N	\N	\N	\N	user1	\N	\N	21:53:13	\N	\N	\N	\N	-	\N	\N	\N	0	\N	customer	\N	\N
24854	\N	\N	user3	\N	user3	\N	\N	\N	\N	\N	user3	\N	\N	\N	\N	\N	\N	user3	\N	\N	09:11:44	\N	\N	92877af70a45fd6a2ed7fe81e1236b78	\N	-	\N	\N	\N	0	\N	customer	2011-04-06 14:52:16.17	\N
\.


--
-- Data for Name: drive_route_point; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY drive_route_point (drive_id, route_idx, coordinate, expected_arrival, seats_available, coordinate_c, distance_to_source) FROM stdin;
55901	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55901	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
55901	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55901	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55901	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55901	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
55902	2	(11.4536761072354647,53.3166703578085546)	2017-07-01 00:23:06.594	1	0101000020FC0B000032228B258790F9C0AFCD2B7B4B6AFB40	11468.9993540472751
55902	3	(11.4896395017871153,53.324606157644233)	2017-07-01 00:27:16.597	1	0101000020FC0B0000AEB4E86F25F9F8C00B0B3EC6F69CFB40	15948.6811617138446
55902	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:12:36.458	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55902	4	(11.527197320622145,53.3472497764360085)	2017-07-01 00:32:03.148	1	0101000020FC0B00000C7E09B52C58F8C048927082CC35FC40	21375.9671340302157
55902	5	(11.5714483686860294,53.3778573210058482)	2017-07-01 00:36:12.303	1	0101000020FC0B0000EEC9C4E7EF99F7C08A434D184D05FD40	27324.079182978483
55902	6	(11.587039472953343,53.3825755337147854)	2017-07-01 00:37:35.161	1	0101000020FC0B000018794B612D58F7C01BA35E074124FD40	29261.2483598599283
55902	7	(11.5380429447749915,53.3933537839357655)	2017-07-01 00:41:10.815	1	0101000020FC0B000088294D67AB21F8C0A65AECDB1A75FD40	34847.5421902792223
55902	1	(11.413155564959844,53.2934183990974333)	2017-07-01 00:18:08.797	1	0101000020FC0B000096A728303D3EFAC07B7B29F3C6CDFA40	5855.44316136799807
55902	8	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:45:32.916	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	40208.4941732670413
55951	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
55951	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55951	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55951	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55951	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55951	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56001	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56001	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56001	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56001	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56001	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56001	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55952	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55952	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
55952	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
55952	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55952	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55952	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55953	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55953	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55953	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55953	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
55953	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
55953	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55954	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55954	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55954	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55954	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
55954	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
55954	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55955	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
55955	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
55955	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
55955	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
55955	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
55955	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56051	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56051	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56051	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56051	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56051	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56051	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56052	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56052	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56052	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56052	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56052	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56052	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56101	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56101	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56101	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56101	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56101	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56101	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56151	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56151	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56151	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56151	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56151	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56151	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56251	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56251	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56251	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56251	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56251	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
56251	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56252	5	(11.4971598281581588,53.4090213635754907)	2017-07-01 00:22:05.524	1	0101000020FC0B0000D070883654C8F8C0631033B816E7FD40	25777.8666383670716
56252	0	(11.3668551553295369,53.2764918887278824)	2017-07-01 00:00:52.762	1	0101000020FC0B0000EAACB0C7D302FBC05D9126901B5EFA40	0
56252	1	(11.3816856156666351,53.3209203553405473)	2017-07-01 00:07:12.495	1	0101000020FC0B0000647BB27E51BBFAC00C7F18BE1F91FB40	5844.19333739001013
56252	2	(11.415943944810568,53.3442123609232652)	2017-07-01 00:11:02.078	1	0101000020FC0B000038DE41F2B027FAC091C73123A72EFC40	10763.287188686656
56252	4	(11.494642194240738,53.378353715949217)	2017-07-01 00:18:14.429	1	0101000020FC0B00003AFF130E29D9F8C0D6993BD31F12FD40	21814.9119239529864
56252	3	(11.4643689944826583,53.3598485222425012)	2017-07-01 00:14:53.985	1	0101000020FC0B0000E2BBADC4EA5AF9C031DE424E3895FC40	16579.779051506197
\.


--
-- Data for Name: driverundertakesride; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY driverundertakesride (ride_id, ride_weekdays, ride_series_id, ride_starttime, ride_comment, ride_acceptable_detour_in_min, ride_offeredseats_no, cust_id, ride_startpt, ride_endpt, ride_currpos, startpt_addr, endpt_addr, ride_acceptable_detour_in_km, ride_acceptable_detour_in_percent, ride_route_point_distance_meters, last_matching_state, is_countermanded) FROM stdin;
55901	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
55902	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
55951	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
56001	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
55952	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
55953	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
55954	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
55955	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	UFAT Bildungswerk, W�bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europ�ische Union	10	\N	6000	\N	\N
56051	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�?e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
56052	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�?e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
56101	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
56151	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�?e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
56251	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�?e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
56252	\N	\N	2017-06-30 23:39:40	Ein Kommentar zum Angebot	\N	1	24852	(11.3666578999999999,53.2764929000000009)	(11.4969985748317001,53.4097713999999968)	\N	Forsthof Glaisin, Lindenstra�?e, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	10	\N	6000	\N	\N
\.


--
-- Data for Name: favoritepoint; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY favoritepoint (favpt_point, cust_id, favpt_frequency, favpt_displayname, favpt_address, favpt_id) FROM stdin;
53.2765605,11.3716255	24852	0	Glaisin, Dorfstrasse	Dorfstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	24901
53.4097714,11.4969985748317	24852	0	Wöbbelin, UFAT	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	24902
53.2764929,11.3666579	24852	0	Forsthof Glaisin	Forsthof Glaisin, Lindenstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	24903
53.3993051,11.5029651	24852	0	Wöbbelin, Museum	Mahn und Gedenkstätten Wöbbelin, Neustädter Straße, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	24904
53.3300973,11.3877354	24853	0	Kummer	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	24905
53.3458745,11.4036132	24853	0	Warlow	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	24906
53.32492545,11.4884287485519	24852	0	Schloss Ludwigslust	Schloss Ludwigslust, Schloßfreiheit, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	25351
53.3300973,11.3877354	24852	0	Kummer	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	27103
53.3482628,11.3443589	24852	0	Picher	Picher, Hagenow-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	27104
53.2764929,11.3666579	24853	0	Glaisin Forsthof	Forsthof Glaisin, Lindenstraße, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	27309
53.4097714,11.4969985748317	24853	0	Wöbbelin Ufat	UFAT Bildungswerk, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	27310
53.3060467,11.3682687	24854	0	Göhlen	Göhlen, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	29353
53.3458745,11.4036132	24854	0	Warlow	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	29354
53.3300973,11.3877354	24854	0	Kummer	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	29355
53.39471962596906,11.499683205471046	24854	0	Wöbbelin, Ortseingang	Ludwigsluster Straße, Wöbbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	29958
53.3825267,11.5871649	24852	0	Neustadt-Glewe	Neustadt-Glewe, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32610
53.4258455,11.8475244	24852	0	Parchim	Parchim, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32951
53.3458745,11.4036132	24852	0	Warlow	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32952
53.3060467,11.3682687	24852	0	Göhlen	Göhlen, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32953
53.3176818,11.4217853	24852	0	Mäthus	Mäthus, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	32954
53.2883824,11.4893222	24852	0	Karstädt	Karstädt, Grabow, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32955
53.23294655,11.4316556934262	24852	0	Eldena	Eldena, Grabow, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	32956
53.32492545,11.4884287485519	24853	0	Schloß Ludwigslust	Schloss Ludwigslust, Schloßfreiheit, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, Europäische Union	32256
\.


--
-- Name: favoritepoints_favpt_id_seq; Type: SEQUENCE SET; Schema: public; Owner: openride
--

SELECT pg_catalog.setval('favoritepoints_favpt_id_seq', 1, false);


--
-- Data for Name: match; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY match (riderroute_id, ride_id, match_shared_distance_meters, match_detour_meters, match_expected_start_time, match_drive_remaining_distance_meters, match_price_cents, driver_change, rider_change, driver_access, rider_access, driver_state, rider_state, rider_message, driver_message) FROM stdin;
55904	55901	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55901	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55902	3618.37858008455532	6451.59595796756184	2017-07-01 00:18:08.797	46660.0901312346032	250	\N	\N	\N	\N	0	0	\N	\N
55904	55902	3618.37858008455532	6451.59595796756184	2017-07-01 00:18:08.797	46660.0901312346032	250	\N	\N	\N	\N	0	0	\N	\N
55904	55951	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55951	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56001	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56001	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	55952	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55952	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55953	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	55953	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	55954	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55954	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	55955	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	55955	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56051	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56051	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56052	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56052	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56101	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56101	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55951	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55955	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56001	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56051	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55953	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55901	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55902	3618.37858008455532	6451.59595796756184	2017-07-01 00:18:08.797	46660.0901312346032	250	\N	\N	\N	\N	0	0	\N	\N
56102	56052	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56101	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55954	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	55952	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56051	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55952	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55954	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55953	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55901	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55902	3618.37858008455532	6451.59595796756184	2017-07-01 00:18:08.797	46660.0901312346032	250	\N	\N	\N	\N	0	0	\N	\N
56103	56001	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56052	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55951	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	55955	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56101	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56151	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56151	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56151	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56151	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56251	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56251	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56251	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56251	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56103	56252	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55905	56252	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
55904	56252	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
56102	56252	3618.37858008455532	1638.23510650864773	2017-07-01 00:07:12.495	27416.1017448757193	125	\N	\N	\N	\N	0	0	\N	\N
\.


--
-- Data for Name: registration_pass; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY registration_pass (id, passcode, creation_date, usage_date, cust_id) FROM stdin;
\.


--
-- Name: registration_pass_id_seq; Type: SEQUENCE SET; Schema: public; Owner: openride
--

SELECT pg_catalog.setval('registration_pass_id_seq', 4, true);


--
-- Data for Name: riderundertakesride; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY riderundertakesride (riderroute_id, ride_id, starttime_latest, timestamprealized, price, cust_id, no_passengers, timestampbooked, account_timestamp, starttime_earliest, startpt, endpt, startpt_addr, endpt_addr, givenrating, givenrating_comment, givenrating_date, receivedrating, receivedrating_comment, receivedrating_date, startpt_c, endpt_c, comment, last_matching_state, is_countermanded) FROM stdin;
55904	\N	2017-07-01 07:53:40	\N	123	24853	1	\N	\N	2017-06-19 05:53:40	(11.3877354000000004,53.3300972999999985)	(11.4036132000000006,53.3458745000000008)	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	\N	\N	\N	\N	\N	\N	0101000020FC0B000066ECB32922A0FAC0973403DF26D0FB40	0101000020FC0B00006CA4C088A25AFAC00DBC2437CE3BFC40		\N	\N
55905	\N	2017-07-01 07:53:40	\N	123	24853	1	\N	\N	2017-06-19 05:53:40	(11.3877354000000004,53.3300972999999985)	(11.4036132000000006,53.3458745000000008)	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	\N	\N	\N	\N	\N	\N	0101000020FC0B000066ECB32922A0FAC0973403DF26D0FB40	0101000020FC0B00006CA4C088A25AFAC00DBC2437CE3BFC40		\N	\N
56102	\N	2017-07-01 07:53:40	\N	123	24853	1	\N	\N	2017-06-19 05:53:40	(11.3877354000000004,53.3300972999999985)	(11.4036132000000006,53.3458745000000008)	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	\N	\N	\N	\N	\N	\N	0101000020FC0B000066ECB32922A0FAC0973403DF26D0FB40	0101000020FC0B00006CA4C088A25AFAC00DBC2437CE3BFC40		\N	\N
56103	\N	2017-07-01 07:53:40	\N	123	24853	1	\N	\N	2017-06-19 05:53:40	(11.3877354000000004,53.3300972999999985)	(11.4036132000000006,53.3458745000000008)	Kummer, Ludwigslust, Mecklenburg-Vorpommern, Deutschland	Warlow, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, Deutschland	\N	\N	\N	\N	\N	\N	0101000020FC0B000066ECB32922A0FAC0973403DF26D0FB40	0101000020FC0B00006CA4C088A25AFAC00DBC2437CE3BFC40		\N	\N
\.


--
-- Data for Name: route_point; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY route_point (ride_id, route_idx, longitude, latitude, riderroute_id, is_required) FROM stdin;
55902	195	11.5687924229083752	53.3870109182673147	\N	f
55902	117	11.5010319985679992	53.3268564192993111	\N	f
55902	76	11.4898505394872394	53.3243325350656932	\N	f
55902	66	11.4899419953661113	53.3218542856392475	\N	f
55902	160	11.5816536154222547	53.3832317974395707	\N	f
55902	51	11.4777068379008806	53.3201225844053042	\N	f
55902	149	11.5696317308313787	53.3765514204625333	\N	f
55902	220	11.5042579137740564	53.3995779991055599	\N	f
55902	128	11.5031678938083619	53.3326365798556026	\N	f
55902	179	11.5839955192207285	53.3833711232998454	\N	f
55902	27	11.4222979863501628	53.3009477697524687	\N	f
55902	170	11.5853284281193112	53.3830872561727574	\N	f
55902	227	11.5030134805220392	53.400697635129788	\N	f
55902	40	11.4502661626845494	53.3169586952841001	\N	f
55902	65	11.4894672071069603	53.3217883479994939	\N	f
55902	88	11.493291031419286	53.3243938160924742	\N	f
55902	30	11.4267966470151396	53.3003696046852227	\N	f
55902	106	11.4971571377774442	53.3246203137476797	\N	f
55902	96	11.4969325027674252	53.3244625477000156	\N	f
55902	199	11.5563292776717823	53.3891239029718321	\N	f
55902	143	11.527197320622145	53.3472497764360085	\N	f
55902	132	11.5039323233946931	53.333863504242828	\N	f
55902	80	11.4896395017871153	53.324606157644233	\N	t
55902	158	11.5796730647908088	53.3824260171300011	\N	f
55902	164	11.5823496859300814	53.3837408583702313	\N	f
55902	144	11.5323266729487486	53.3502300087413914	\N	f
55902	107	11.4971491284031231	53.3247680215113178	\N	f
55902	29	11.4263673072986442	53.3004128180536512	\N	f
55902	155	11.5771249661697073	53.3815401430772312	\N	f
55902	139	11.5111765230710024	53.3380414174059325	\N	f
55902	92	11.4953259712903044	53.324432372847923	\N	f
55902	234	11.4981992877617323	53.4091007726438534	\N	f
55902	37	11.4449956218524793	53.3142828192029015	\N	f
55902	174	11.587039472953343	53.3825755337147854	\N	t
55902	101	11.4973312951027893	53.3244252947961996	\N	f
55902	58	11.4820400956729074	53.3207383749054031	\N	f
55902	79	11.4896395017871153	53.324606157644233	\N	t
55902	172	11.5869716537066925	53.3825135614539761	\N	f
55902	134	11.5073994511529687	53.3358393982612924	\N	f
55902	221	11.5041161664750309	53.3995895475057409	\N	f
55902	167	11.5835851784851815	53.3834530796882447	\N	f
55902	111	11.4970818869117331	53.3261046557002842	\N	f
55902	69	11.4900647436841901	53.3228049797446673	\N	f
55902	200	11.5533009891204763	53.3896424633929669	\N	f
55902	114	11.4996061436743915	53.3260621873899296	\N	f
55902	75	11.4899252315593934	53.324333838917326	\N	f
55902	22	11.394941502696458	53.2817941892548106	\N	f
55902	17	11.3804683770344042	53.2778416561597936	\N	f
55902	178	11.5851188805353384	53.38314164541233	\N	f
55902	60	11.4859436411994018	53.3212982860497746	\N	f
55902	177	11.5853284281193112	53.3830872561727574	\N	f
55902	184	11.5819905679372823	53.3838185306746951	\N	f
55902	235	11.4971598281581588	53.4090213635754907	\N	t
55902	6	11.3687219776672563	53.2772510113697706	\N	f
55951	42	11.406470903898871	53.3436977120570361	\N	f
55951	80	11.4677370295167798	53.3605015656464161	\N	f
55951	106	11.5026904978459434	53.39931816010143	\N	f
55951	23	11.3736119800868334	53.3083005616434065	\N	f
55951	115	11.5009068288111731	53.4059102476964185	\N	f
55951	105	11.5019754283571718	53.3980405717650157	\N	f
55951	39	11.404349723555514	53.3416935058316639	\N	f
55951	71	11.4497189175274734	53.3585068589115181	\N	f
55951	24	11.3761535594497669	53.3121620113885868	\N	f
55951	20	11.3731329077437433	53.305191248026297	\N	f
55951	64	11.4457503656838178	53.3544299011177543	\N	f
55951	96	11.491843569841464	53.3647046245196037	\N	f
55951	19	11.3723826342608625	53.3038028323010238	\N	f
55951	118	11.4981992877617323	53.4091007726438534	\N	f
55951	92	11.4885565598731461	53.3581477409187173	\N	f
55951	46	11.4082055853651223	53.344018087029859	\N	f
55951	45	11.4081450493964187	53.3440229299073607	\N	f
55951	47	11.4092112275036701	53.3438225092848199	\N	f
55951	111	11.5030134805220392	53.400697635129788	\N	f
55951	22	11.3747675651632445	53.3081286394922884	\N	f
55951	83	11.4706516967114442	53.3601867786091617	\N	f
55951	97	11.4922609886287379	53.3667406819777312	\N	f
55951	73	11.451422865348075	53.3591833716448392	\N	f
55951	10	11.3663169301968132	53.2862775762292173	\N	f
55951	3	11.3668621264441789	53.2774149241465693	\N	f
55951	30	11.386493289168774	53.3283707498394861	\N	f
55951	41	11.4061214716610646	53.3434499802466462	\N	f
55951	68	11.4479296605571292	53.3564456557433076	\N	f
55951	6	11.3667265258742844	53.2802837702695413	\N	f
55951	116	11.5001327134698492	53.4080377610334267	\N	f
55951	82	11.4697490588519511	53.3604240796064744	\N	f
55951	12	11.366174437839712	53.2890549664733157	\N	f
55951	95	11.4914300626090924	53.3628423518577719	\N	f
55951	7	11.3666896454995054	53.2809176284279857	\N	f
55951	4	11.3668269224500715	53.2781605410164687	\N	f
55951	15	11.3662007011369024	53.2969300440757721	\N	f
55951	110	11.5030790456327576	53.4003658980212919	\N	f
55951	40	11.4053866581332688	53.3427775653327458	\N	f
55951	101	11.4973564408128652	53.3908770246254747	\N	f
55951	8	11.3666140221047556	53.2822030398742044	\N	f
55951	72	11.4503570597698623	53.358840831194243	\N	f
55951	32	11.389851265918864	53.3305286242931018	\N	f
55951	87	11.4745487229797707	53.3591105422178771	\N	f
55951	84	11.4708519310694612	53.3601357421309288	\N	f
55951	99	11.494642194240738	53.378353715949217	\N	f
55951	117	11.4998847953949443	53.409178817477347	\N	f
55951	103	11.4995448626476122	53.3945687873937658	\N	f
55951	14	11.3658294759503633	53.2957770667026267	\N	f
55951	25	11.3816856156666351	53.3209203553405473	\N	f
55951	48	11.4103845077093951	53.3436043935329707	\N	f
55951	70	11.4490124162065783	53.3579167729150541	\N	f
55951	54	11.4276730215774407	53.3505228165653946	\N	f
55951	76	11.4621861605834852	53.3596225833808475	\N	f
55951	60	11.4420364374377534	53.3534346897922731	\N	f
55951	109	11.5030555763033533	53.4000395625838564	\N	f
55951	62	11.4449837009232578	53.3540961150995514	\N	f
55951	31	11.3876367670514451	53.3291055633672855	\N	f
55951	21	11.3740713083908993	53.3068184548650308	\N	f
55951	61	11.4441822046976291	53.3538446579987848	\N	f
55951	107	11.5027465634661894	53.399416880296549	\N	f
55951	78	11.4643689944826583	53.3598485222425012	\N	f
55951	113	11.5017889775735664	53.4028037280470969	\N	f
55951	55	11.4286229706247813	53.3509664986498606	\N	f
55951	63	11.4454696650535546	53.3543056626835224	\N	f
55951	11	11.3661854274463376	53.2888593887282767	\N	f
55951	86	11.4731929035453408	53.3594795322301891	\N	f
55901	115	11.5009068288111731	53.4059102476964185	\N	f
55901	0	11.3668551553295369	53.2764918887278824	\N	t
55901	14	11.3658294759503633	53.2957770667026267	\N	f
55901	118	11.4981992877617323	53.4091007726438534	\N	f
55901	80	11.4677370295167798	53.3605015656464161	\N	f
55901	53	11.4255568703761003	53.3494385707997978	\N	f
55901	49	11.4115488472182047	53.3433557303999919	\N	f
55901	40	11.4053866581332688	53.3427775653327458	\N	f
55901	16	11.3679217852932606	53.3022754632445128	\N	f
55901	87	11.4745487229797707	53.3591105422178771	\N	f
55901	69	11.448233457987758	53.3568584179176071	\N	f
55901	74	11.4523132097493079	53.3593722438671918	\N	f
55901	27	11.3840211864714593	53.3245972169473177	\N	f
55901	84	11.4708519310694612	53.3601357421309288	\N	f
55901	5	11.3667831502880858	53.2790801289472	\N	f
55901	11	11.3661854274463376	53.2888593887282767	\N	f
55901	111	11.5030134805220392	53.400697635129788	\N	f
55901	3	11.3668621264441789	53.2774149241465693	\N	f
55901	113	11.5017889775735664	53.4028037280470969	\N	f
55901	75	11.4534518447544844	53.3594795322301891	\N	f
55901	92	11.4885565598731461	53.3581477409187173	\N	f
55901	48	11.4103845077093951	53.3436043935329707	\N	f
55901	33	11.3910042432920093	53.3310145884234004	\N	f
55901	104	11.5005937181545885	53.3957429989220884	\N	f
55901	72	11.4503570597698623	53.358840831194243	\N	f
55901	82	11.4697490588519511	53.3604240796064744	\N	f
55901	63	11.4454696650535546	53.3543056626835224	\N	f
55901	30	11.386493289168774	53.3283707498394861	\N	f
55901	70	11.4490124162065783	53.3579167729150541	\N	f
55901	81	11.4687290743454344	53.3605332306146565	\N	f
55901	105	11.5019754283571718	53.3980405717650157	\N	f
55901	38	11.4021251664040655	53.3394978196806733	\N	f
55901	97	11.4922609886287379	53.3667406819777312	\N	f
55901	98	11.492782529282179	53.3692813300180759	\N	f
55901	88	11.4797164457973047	53.3590611821203211	\N	f
55901	89	11.4835570339163482	53.3588307729102098	\N	f
55901	109	11.5030555763033533	53.4000395625838564	\N	f
55901	96	11.491843569841464	53.3647046245196037	\N	f
55901	57	11.4316508866470503	53.3516415212670339	\N	f
55901	107	11.5027465634661894	53.399416880296549	\N	f
55901	77	11.4633732243636235	53.3597050985627988	\N	f
55901	37	11.3976618959977163	53.3350390196227835	\N	f
55901	47	11.4092112275036701	53.3438225092848199	\N	f
55901	10	11.3663169301968132	53.2862775762292173	\N	f
55901	116	11.5001327134698492	53.4080377610334267	\N	f
55901	64	11.4457503656838178	53.3544299011177543	\N	f
55901	73	11.451422865348075	53.3591833716448392	\N	f
55901	35	11.3947306512608524	53.3323100581536451	\N	f
55901	60	11.4420364374377534	53.3534346897922731	\N	f
55901	76	11.4621861605834852	53.3596225833808475	\N	f
55901	22	11.3747675651632445	53.3081286394922884	\N	f
55901	39	11.404349723555514	53.3416935058316639	\N	f
55901	41	11.4061214716610646	53.3434499802466462	\N	f
55901	52	11.415943944810568	53.3442123609232652	\N	f
55901	101	11.4973564408128652	53.3908770246254747	\N	f
55901	85	11.4710493714596939	53.3600781863945315	\N	f
55901	28	11.3853159111436284	53.326561935094638	\N	f
55901	79	11.4668388620057442	53.3603601908764276	\N	f
55901	119	11.4971598281581588	53.4090213635754907	\N	t
55901	86	11.4731929035453408	53.3594795322301891	\N	f
55901	100	11.4970470554466626	53.3892911685099705	\N	f
55901	78	11.4643689944826583	53.3598485222425012	\N	f
55901	54	11.4276730215774407	53.3505228165653946	\N	f
55901	110	11.5030790456327576	53.4003658980212919	\N	f
55901	17	11.3683809273328098	53.3039624609938798	\N	f
55901	29	11.3860019233674237	53.3276478572409118	\N	f
55901	108	11.5030032359734893	53.3998456612194872	\N	f
55901	24	11.3761535594497669	53.3121620113885868	\N	f
55901	59	11.4408866265614328	53.3532530818861659	\N	f
55901	55	11.4286229706247813	53.3509664986498606	\N	f
55901	51	11.413180338140883	53.3427071573445346	\N	f
55901	46	11.4082055853651223	53.344018087029859	\N	f
55901	19	11.3723826342608625	53.3038028323010238	\N	f
55901	103	11.4995448626476122	53.3945687873937658	\N	f
55901	20	11.3731329077437433	53.305191248026297	\N	f
55901	34	11.3921857191365739	53.3314146846103938	\N	f
55901	61	11.4441822046976291	53.3538446579987848	\N	f
55901	102	11.4979098326990723	53.3925997851625027	\N	f
55901	93	11.4892433171550188	53.3578916272049781	\N	f
55901	7	11.3666896454995054	53.2809176284279857	\N	f
55901	95	11.4914300626090924	53.3628423518577719	\N	f
55901	45	11.4081450493964187	53.3440229299073607	\N	f
55901	71	11.4497189175274734	53.3585068589115181	\N	f
55901	106	11.5026904978459434	53.39931816010143	\N	f
55901	31	11.3876367670514451	53.3291055633672855	\N	f
55901	25	11.3816856156666351	53.3209203553405473	\N	f
55901	91	11.4874980186111788	53.3584722137109679	\N	f
55901	112	11.5028480776290909	53.4010761246325671	\N	f
55901	62	11.4449837009232578	53.3540961150995514	\N	f
55901	13	11.3658399067634317	53.2955755284929751	\N	f
55901	23	11.3736119800868334	53.3083005616434065	\N	f
55901	21	11.3740713083908993	53.3068184548650308	\N	f
55901	36	11.3951650201193626	53.3324909210016784	\N	f
55901	44	11.4075396897093881	53.3440726625339536	\N	f
55901	12	11.366174437839712	53.2890549664733157	\N	f
55901	18	11.3683557816227321	53.3043018349476583	\N	f
55901	68	11.4479296605571292	53.3564456557433076	\N	f
55901	9	11.3663862205979136	53.2847749803537525	\N	f
55901	99	11.494642194240738	53.378353715949217	\N	f
55901	65	11.4462832684729232	53.3547269930257002	\N	f
55901	117	11.4998847953949443	53.409178817477347	\N	f
55901	4	11.3668269224500715	53.2781605410164687	\N	f
55901	83	11.4706516967114442	53.3601867786091617	\N	f
55901	6	11.3667265258742844	53.2802837702695413	\N	f
55901	114	11.5011450611310835	53.4050720573605346	\N	f
55901	26	11.3827156584571814	53.3225242791144041	\N	f
55901	43	11.4068983809701745	53.3438806238147762	\N	f
55901	90	11.486310768566522	53.3586301660231541	\N	f
55901	56	11.4291759899819496	53.3511384208009787	\N	f
55901	2	11.3668634302958118	53.277110195393341	\N	f
55901	15	11.3662007011369024	53.2969300440757721	\N	f
55901	50	11.4124784934329657	53.3430893721376975	\N	f
55901	32	11.389851265918864	53.3305286242931018	\N	f
55901	67	11.4471706326418516	53.3554709335149298	\N	f
55901	94	11.4903014858879473	53.3576314156718112	\N	f
55901	58	11.4369724639573498	53.3526151259082937	\N	f
55901	8	11.3666140221047556	53.2822030398742044	\N	f
55901	1	11.3668597050054299	53.2768091919304965	\N	f
55901	66	11.4467701639258159	53.355085924753979	\N	f
55901	42	11.406470903898871	53.3436977120570361	\N	f
55902	85	11.4915086662361468	53.3243621511242267	\N	f
55902	140	11.512960378370293	53.3390749992223476	\N	f
55902	217	11.506685126722271	53.3992268904870784	\N	f
55902	18	11.3821101125056323	53.2777451711389105	\N	f
55902	86	11.4921792185048588	53.3243740720534518	\N	f
55902	190	11.5786718930007204	53.3846919250046881	\N	f
55902	161	11.5817778538564866	53.3835382025734688	\N	f
55902	113	11.4992751516239746	53.3260616285963707	\N	f
55902	150	11.5698172502923882	53.3766847858582025	\N	f
55902	115	11.5000300817198333	53.3260621873899296	\N	f
55902	4	11.3675405018226918	53.2772269832468126	\N	f
55902	125	11.5027741306150144	53.3307716994905121	\N	f
55902	131	11.5031146221559037	53.3333935588611681	\N	f
55902	224	11.5030032359734893	53.3998456612194872	\N	f
55902	203	11.5479222286028165	53.3910878760610785	\N	f
55902	89	11.4944011679530398	53.3244148639831295	\N	f
55902	135	11.5081357547969159	53.3362653852164428	\N	f
55902	210	11.5240384606429593	53.3959743394547957	\N	f
55902	118	11.5011916272608552	53.3270853383932746	\N	f
55902	82	11.4896365215548091	53.3243289960398315	\N	f
55902	151	11.5714483686860294	53.3778573210058482	\N	f
55902	232	11.5001327134698492	53.4080377610334267	\N	f
55902	16	11.3791377033100503	53.2780068727882252	\N	f
55902	226	11.5030790456327576	53.4003658980212919	\N	f
55902	77	11.4896365215548091	53.3243289960398315	\N	f
55902	127	11.5030566938904677	53.3318363874816086	\N	f
55902	47	11.4736378894814379	53.3195559677382391	\N	f
55902	42	11.4528354954608265	53.3166325461111796	\N	f
55902	216	11.508956250003493	53.3987843259897303	\N	f
55902	126	11.5029261224625881	53.3312859758277114	\N	f
55902	91	11.4951458535003486	53.3244288338220613	\N	f
55902	231	11.5009068288111731	53.4059102476964185	\N	f
55902	110	11.4970936215764343	53.3257175980296196	\N	f
55902	136	11.5084626490279138	53.3364497870903449	\N	f
55902	159	11.5814297254703131	53.3830242987653065	\N	f
55902	212	11.5224354681917021	53.3962744115950443	\N	f
55902	133	11.5054153614956594	53.3346912637656487	\N	f
55902	229	11.5017889775735664	53.4028037280470969	\N	f
55902	213	11.5200898391028463	53.3967125057439347	\N	f
55902	50	11.475720326804824	53.3198403936588861	\N	f
55902	44	11.4652902587940595	53.3183152597766039	\N	f
55902	123	11.5019430183308504	53.3297016098283621	\N	f
55902	138	11.5101649204678438	53.3374563605514851	\N	f
55902	171	11.5855130162577264	53.3830269064685723	\N	f
55902	105	11.4972202814494135	53.3246108142572055	\N	f
55902	31	11.4282528630253566	53.3008278154021724	\N	f
55902	141	11.515240814877469	53.3403859289076792	\N	f
55902	130	11.5031436794208819	53.3330830559078564	\N	f
55902	183	11.5823496859300814	53.3837408583702313	\N	f
55902	11	11.3747468898016262	53.277850410592194	\N	f
55902	23	11.4036231056665578	53.2854164753574864	\N	f
55902	193	11.5733842158328937	53.3859968942254071	\N	f
55902	165	11.5826121326374736	53.3836870279242177	\N	f
55902	83	11.4898505394872394	53.3243325350656932	\N	f
55902	222	11.5035795383955435	53.3995431676404877	\N	f
55902	62	11.4881525521312486	53.3216054362417466	\N	f
55902	74	11.4900420194128614	53.323742635333744	\N	f
55902	218	11.5050793403032277	53.3995331093564545	\N	f
55902	46	11.4734110192971901	53.3195248615635506	\N	f
55902	223	11.5034502708192967	53.3995336681500135	\N	f
55902	182	11.5826121326374736	53.3836870279242177	\N	f
55902	176	11.5855130162577264	53.3830269064685723	\N	f
55902	202	11.5496133241716024	53.3906201658536546	\N	f
55902	100	11.4972713179276429	53.3243859929826698	\N	f
55902	53	11.4808180141631819	53.3205647763736152	\N	f
55902	14	11.3769593397593347	53.2784764456408411	\N	f
55902	45	11.4669601202076699	53.3185497668061359	\N	f
55902	68	11.490264978042207	53.3222340789936666	\N	f
55902	181	11.5831912290273138	53.383548633386539	\N	f
55902	187	11.5815247203750467	53.3839446317541118	\N	f
55902	32	11.4371438273149089	53.3037877448749811	\N	f
55902	116	11.5004698522493953	53.3260513840478225	\N	f
55902	145	11.5346832916442299	53.3515545357366179	\N	f
55902	25	11.413155564959844	53.2934183990974333	\N	f
55902	54	11.4815831888075888	53.3206733685882384	\N	f
55902	207	11.5382662759333758	53.3933081491285861	\N	f
55902	157	11.5778500939425104	53.3817934628231825	\N	f
55902	194	11.5712144204500564	53.3865351986855714	\N	f
55902	162	11.5818646531223806	53.3836290996587834	\N	f
55902	233	11.4998847953949443	53.409178817477347	\N	f
55902	15	11.3786353479020743	53.2780763494538476	\N	f
55902	52	11.4795957463889362	53.3203909915773053	\N	f
55902	152	11.5750531459239081	53.3804588775439299	\N	f
55902	204	11.5468523252051849	53.3913646651364431	\N	f
55902	98	11.4970593489049229	53.3243729544663339	\N	f
55902	56	11.4818184408951947	53.3207068962016777	\N	f
55902	49	11.4750302167616081	53.3197459575477097	\N	f
55902	219	11.5046192669410843	53.3995867535379531	\N	f
55902	175	11.5869716537066925	53.3825135614539761	\N	f
55902	38	11.4474278639427087	53.317564241235651	\N	f
55902	228	11.5028480776290909	53.4010761246325671	\N	f
55902	0	11.3668551553295369	53.2764918887278824	\N	t
55902	2	11.3668634302958118	53.277110195393341	\N	f
55902	208	11.5380429447749915	53.3933537839357655	\N	f
55902	197	11.5624212450330237	53.3883926284699015	\N	f
55902	90	11.4946505761440978	53.3244195205961091	\N	f
55902	205	11.5455745506042522	53.3916796384382195	\N	f
55902	78	11.4895748679989911	53.3243263883365657	\N	f
55902	71	11.4901586210018092	53.3231264723046081	\N	f
55902	209	11.5245527369801568	53.3958780406984275	\N	f
55902	154	11.5762741098465192	53.3811214204383191	\N	f
55902	122	11.5017418526502375	53.3293059839898191	\N	f
55902	7	11.3692561843079964	53.277196994659235	\N	f
55902	5	11.3680981777928363	53.2773005577318486	\N	f
55902	180	11.5835851784851815	53.3834530796882447	\N	f
55902	146	11.5385330067247072	53.354125358629048	\N	f
55902	225	11.5030555763033533	53.4000395625838564	\N	f
55902	61	11.4878984873272145	53.3215700459831226	\N	f
55902	103	11.4973396770061473	53.3245461804690848	\N	f
55902	124	11.5022328459225491	53.3300456403951131	\N	f
55902	59	11.4849892218036036	53.321158028866904	\N	f
55902	148	11.5436636629029437	53.3578774711015242	\N	f
55902	41	11.4520582136226796	53.3166627209632722	\N	f
55902	104	11.4972897581150324	53.3245858548116516	\N	f
55902	21	11.3856705587879699	53.2779271515740547	\N	f
55902	48	11.4746584327815118	53.3196952935985138	\N	f
55902	72	11.4900479798774722	53.3233300594239665	\N	f
55902	147	11.5408672736778986	53.3558477466370391	\N	f
55902	63	11.4886040573255137	53.3216682073846826	\N	f
55902	129	11.5031788834149893	53.3328509703170752	\N	f
55902	64	11.4889875759703131	53.3217216653016592	\N	f
55902	189	11.5794383714967601	53.3845252182601087	\N	f
55902	201	11.5503291387184515	53.3904221666698646	\N	f
55902	12	11.3752259621447163	53.2781709718295389	\N	f
55902	10	11.3741169431980769	53.2774287077209792	\N	f
55902	166	11.5831912290273138	53.383548633386539	\N	f
55902	26	11.4171412531392562	53.2968398920485384	\N	f
55902	153	11.5759751552933867	53.3809655170358468	\N	f
55902	36	11.4437571490650729	53.3125511179689582	\N	f
55902	198	11.5595767995620555	53.3886588004676739	\N	f
55902	34	11.4379559406181261	53.3046939217603324	\N	f
55902	39	11.4476223241006352	53.317525684480195	\N	f
55902	8	11.3717683338769149	53.2765735673138536	\N	f
55902	73	11.4898935665911495	53.3235058931299903	\N	f
55902	230	11.5011450611310835	53.4050720573605346	\N	f
55902	94	11.4964804387796029	53.3244541657966593	\N	f
55902	108	11.4971420503513979	53.3248961715004484	\N	f
55902	95	11.4966782516988726	53.3244578910870359	\N	f
55902	211	11.5229363334835249	53.3961807205419419	\N	f
55902	102	11.4973705969163156	53.3244707433388569	\N	f
55902	188	11.5811786408985853	53.3840807911175688	\N	f
55902	28	11.4233723600962538	53.3007451139556991	\N	f
55902	120	11.5015825964864185	53.3284256978726177	\N	f
55902	168	11.5839955192207285	53.3833711232998454	\N	f
55902	185	11.5818339194767308	53.3838954579210778	\N	f
55902	55	11.4817966479464619	53.3207042884984048	\N	f
55902	169	11.5851188805353384	53.38314164541233	\N	f
55902	24	11.4040842966158156	53.2857245568720543	\N	f
55902	67	11.4903767367536584	53.3219148216079475	\N	f
55902	33	11.4373559826021491	53.303947746096874	\N	f
55902	206	11.5390688897461189	53.3931546671648647	\N	f
55902	119	11.5013991259351176	53.3275690673493372	\N	f
55902	121	11.5016261823838839	53.3286991341866354	\N	f
55902	19	11.3835348498121238	53.2776781159120389	\N	f
55902	214	11.5159545805146077	53.3974847584400649	\N	f
55902	142	11.5252815900433419	53.3461508157733988	\N	f
55902	20	11.3846338104747336	53.2777667778231248	\N	f
55902	81	11.4895748679989911	53.3243263883365657	\N	f
55902	215	11.5092620963438321	53.3987345933631374	\N	f
55902	163	11.5819980185180462	53.3837108697826608	\N	f
55902	97	11.4969749710777762	53.3244128150734156	\N	f
55902	109	11.497122492576894	53.3252297712541292	\N	f
55902	186	11.5816886331518436	53.3838893111919432	\N	f
55902	156	11.5777401978762491	53.3817642192936859	\N	f
55902	84	11.4899252315593934	53.324333838917326	\N	f
55902	93	11.4962038359687586	53.3244483915965617	\N	f
55902	191	11.5778208504130138	53.3848940220078987	\N	f
55902	9	11.3733808258186464	53.2761732848623382	\N	f
55951	89	11.4835570339163482	53.3588307729102098	\N	f
55951	108	11.5030032359734893	53.3998456612194872	\N	f
55951	56	11.4291759899819496	53.3511384208009787	\N	f
55951	36	11.3951650201193626	53.3324909210016784	\N	f
55951	28	11.3853159111436284	53.326561935094638	\N	f
55951	100	11.4970470554466626	53.3892911685099705	\N	f
55951	16	11.3679217852932606	53.3022754632445128	\N	f
55951	49	11.4115488472182047	53.3433557303999919	\N	f
55951	90	11.486310768566522	53.3586301660231541	\N	f
55951	5	11.3667831502880858	53.2790801289472	\N	f
55951	74	11.4523132097493079	53.3593722438671918	\N	f
55951	79	11.4668388620057442	53.3603601908764276	\N	f
55951	88	11.4797164457973047	53.3590611821203211	\N	f
55951	81	11.4687290743454344	53.3605332306146565	\N	f
55951	34	11.3921857191365739	53.3314146846103938	\N	f
55951	67	11.4471706326418516	53.3554709335149298	\N	f
55951	1	11.3668597050054299	53.2768091919304965	\N	f
55951	0	11.3668551553295369	53.2764918887278824	\N	t
55951	50	11.4124784934329657	53.3430893721376975	\N	f
55951	114	11.5011450611310835	53.4050720573605346	\N	f
55951	43	11.4068983809701745	53.3438806238147762	\N	f
55951	91	11.4874980186111788	53.3584722137109679	\N	f
55951	51	11.413180338140883	53.3427071573445346	\N	f
55951	13	11.3658399067634317	53.2955755284929751	\N	f
55951	112	11.5028480776290909	53.4010761246325671	\N	f
55951	35	11.3947306512608524	53.3323100581536451	\N	f
55951	85	11.4710493714596939	53.3600781863945315	\N	f
55951	57	11.4316508866470503	53.3516415212670339	\N	f
55951	102	11.4979098326990723	53.3925997851625027	\N	f
55951	29	11.3860019233674237	53.3276478572409118	\N	f
55951	59	11.4408866265614328	53.3532530818861659	\N	f
55951	104	11.5005937181545885	53.3957429989220884	\N	f
55951	18	11.3683557816227321	53.3043018349476583	\N	f
55951	53	11.4255568703761003	53.3494385707997978	\N	f
55951	65	11.4462832684729232	53.3547269930257002	\N	f
55951	119	11.4971598281581588	53.4090213635754907	\N	t
55951	77	11.4633732243636235	53.3597050985627988	\N	f
55951	52	11.415943944810568	53.3442123609232652	\N	f
55951	17	11.3683809273328098	53.3039624609938798	\N	f
55951	2	11.3668634302958118	53.277110195393341	\N	f
55951	94	11.4903014858879473	53.3576314156718112	\N	f
55951	69	11.448233457987758	53.3568584179176071	\N	f
55951	44	11.4075396897093881	53.3440726625339536	\N	f
55951	37	11.3976618959977163	53.3350390196227835	\N	f
55951	58	11.4369724639573498	53.3526151259082937	\N	f
55951	9	11.3663862205979136	53.2847749803537525	\N	f
55951	98	11.492782529282179	53.3692813300180759	\N	f
55951	93	11.4892433171550188	53.3578916272049781	\N	f
55951	33	11.3910042432920093	53.3310145884234004	\N	f
55951	66	11.4467701639258159	53.355085924753979	\N	f
55951	75	11.4534518447544844	53.3594795322301891	\N	f
55951	26	11.3827156584571814	53.3225242791144041	\N	f
55951	27	11.3840211864714593	53.3245972169473177	\N	f
55951	38	11.4021251664040655	53.3394978196806733	\N	f
55952	31	11.3876367670514451	53.3291055633672855	\N	f
55952	106	11.5026904978459434	53.39931816010143	\N	f
55952	49	11.4115488472182047	53.3433557303999919	\N	f
55952	3	11.3668621264441789	53.2774149241465693	\N	f
55952	108	11.5030032359734893	53.3998456612194872	\N	f
55952	89	11.4835570339163482	53.3588307729102098	\N	f
55952	66	11.4467701639258159	53.355085924753979	\N	f
55952	2	11.3668634302958118	53.277110195393341	\N	f
55952	7	11.3666896454995054	53.2809176284279857	\N	f
55952	104	11.5005937181545885	53.3957429989220884	\N	f
55952	116	11.5001327134698492	53.4080377610334267	\N	f
55952	44	11.4075396897093881	53.3440726625339536	\N	f
55952	91	11.4874980186111788	53.3584722137109679	\N	f
55952	53	11.4255568703761003	53.3494385707997978	\N	f
55952	79	11.4668388620057442	53.3603601908764276	\N	f
55952	51	11.413180338140883	53.3427071573445346	\N	f
55952	48	11.4103845077093951	53.3436043935329707	\N	f
55952	101	11.4973564408128652	53.3908770246254747	\N	f
55952	92	11.4885565598731461	53.3581477409187173	\N	f
55952	12	11.366174437839712	53.2890549664733157	\N	f
55952	34	11.3921857191365739	53.3314146846103938	\N	f
55952	88	11.4797164457973047	53.3590611821203211	\N	f
55952	58	11.4369724639573498	53.3526151259082937	\N	f
55952	38	11.4021251664040655	53.3394978196806733	\N	f
55952	73	11.451422865348075	53.3591833716448392	\N	f
55952	96	11.491843569841464	53.3647046245196037	\N	f
55952	1	11.3668597050054299	53.2768091919304965	\N	f
55952	13	11.3658399067634317	53.2955755284929751	\N	f
55952	102	11.4979098326990723	53.3925997851625027	\N	f
55952	65	11.4462832684729232	53.3547269930257002	\N	f
55952	70	11.4490124162065783	53.3579167729150541	\N	f
55952	37	11.3976618959977163	53.3350390196227835	\N	f
55952	72	11.4503570597698623	53.358840831194243	\N	f
55952	62	11.4449837009232578	53.3540961150995514	\N	f
55952	27	11.3840211864714593	53.3245972169473177	\N	f
55952	103	11.4995448626476122	53.3945687873937658	\N	f
55952	36	11.3951650201193626	53.3324909210016784	\N	f
55952	52	11.415943944810568	53.3442123609232652	\N	f
55952	56	11.4291759899819496	53.3511384208009787	\N	f
55952	60	11.4420364374377534	53.3534346897922731	\N	f
55952	68	11.4479296605571292	53.3564456557433076	\N	f
55952	107	11.5027465634661894	53.399416880296549	\N	f
55952	50	11.4124784934329657	53.3430893721376975	\N	f
55952	67	11.4471706326418516	53.3554709335149298	\N	f
55952	63	11.4454696650535546	53.3543056626835224	\N	f
55952	82	11.4697490588519511	53.3604240796064744	\N	f
55952	39	11.404349723555514	53.3416935058316639	\N	f
55952	109	11.5030555763033533	53.4000395625838564	\N	f
55952	40	11.4053866581332688	53.3427775653327458	\N	f
55952	105	11.5019754283571718	53.3980405717650157	\N	f
55952	24	11.3761535594497669	53.3121620113885868	\N	f
55952	87	11.4745487229797707	53.3591105422178771	\N	f
55952	28	11.3853159111436284	53.326561935094638	\N	f
55952	41	11.4061214716610646	53.3434499802466462	\N	f
55952	45	11.4081450493964187	53.3440229299073607	\N	f
55952	5	11.3667831502880858	53.2790801289472	\N	f
55952	19	11.3723826342608625	53.3038028323010238	\N	f
55952	75	11.4534518447544844	53.3594795322301891	\N	f
55952	29	11.3860019233674237	53.3276478572409118	\N	f
55952	69	11.448233457987758	53.3568584179176071	\N	f
55952	94	11.4903014858879473	53.3576314156718112	\N	f
55952	35	11.3947306512608524	53.3323100581536451	\N	f
55952	6	11.3667265258742844	53.2802837702695413	\N	f
55952	16	11.3679217852932606	53.3022754632445128	\N	f
55952	97	11.4922609886287379	53.3667406819777312	\N	f
55952	42	11.406470903898871	53.3436977120570361	\N	f
55952	85	11.4710493714596939	53.3600781863945315	\N	f
55952	84	11.4708519310694612	53.3601357421309288	\N	f
55952	32	11.389851265918864	53.3305286242931018	\N	f
55952	54	11.4276730215774407	53.3505228165653946	\N	f
55952	100	11.4970470554466626	53.3892911685099705	\N	f
55902	3	11.3671070642867775	53.2771128030966139	\N	f
55902	99	11.4971705488228171	53.3243651313565294	\N	f
55902	43	11.4536761072354647	53.3166703578085546	\N	f
55902	35	11.4398800531002891	53.3072321483619262	\N	f
55902	192	11.576826942939169	53.3851410087602076	\N	f
55902	70	11.4901586210018092	53.3229506385985914	\N	f
55902	1	11.3668597050054299	53.2768091919304965	\N	f
55902	112	11.4981596134191673	53.3260834215451069	\N	f
55902	173	11.587039472953343	53.3825755337147854	\N	t
55902	13	11.3756174901638367	53.2787882524457928	\N	f
55902	196	11.5642222366680709	53.388153278562875	\N	f
55902	137	11.5091559255679527	53.336840756315901	\N	f
55902	57	11.4819139945934854	53.3207204935115655	\N	f
55902	87	11.4929287469296622	53.3243874830988247	\N	f
56001	112	11.5028480776290909	53.4010761246325671	\N	f
56001	99	11.494642194240738	53.378353715949217	\N	f
56001	81	11.4687290743454344	53.3605332306146565	\N	f
56001	109	11.5030555763033533	53.4000395625838564	\N	f
56001	87	11.4745487229797707	53.3591105422178771	\N	f
56001	40	11.4053866581332688	53.3427775653327458	\N	f
56001	103	11.4995448626476122	53.3945687873937658	\N	f
56001	18	11.3683557816227321	53.3043018349476583	\N	f
56001	16	11.3679217852932606	53.3022754632445128	\N	f
56001	70	11.4490124162065783	53.3579167729150541	\N	f
56001	73	11.451422865348075	53.3591833716448392	\N	f
56001	12	11.366174437839712	53.2890549664733157	\N	f
56001	118	11.4981992877617323	53.4091007726438534	\N	f
56001	83	11.4706516967114442	53.3601867786091617	\N	f
56001	29	11.3860019233674237	53.3276478572409118	\N	f
56001	69	11.448233457987758	53.3568584179176071	\N	f
56001	55	11.4286229706247813	53.3509664986498606	\N	f
56001	32	11.389851265918864	53.3305286242931018	\N	f
56001	34	11.3921857191365739	53.3314146846103938	\N	f
56001	36	11.3951650201193626	53.3324909210016784	\N	f
56001	26	11.3827156584571814	53.3225242791144041	\N	f
56001	28	11.3853159111436284	53.326561935094638	\N	f
56001	8	11.3666140221047556	53.2822030398742044	\N	f
56001	25	11.3816856156666351	53.3209203553405473	\N	f
56001	91	11.4874980186111788	53.3584722137109679	\N	f
56001	106	11.5026904978459434	53.39931816010143	\N	f
56001	76	11.4621861605834852	53.3596225833808475	\N	f
56001	113	11.5017889775735664	53.4028037280470969	\N	f
56001	117	11.4998847953949443	53.409178817477347	\N	f
56001	4	11.3668269224500715	53.2781605410164687	\N	f
56001	61	11.4441822046976291	53.3538446579987848	\N	f
56001	27	11.3840211864714593	53.3245972169473177	\N	f
56001	17	11.3683809273328098	53.3039624609938798	\N	f
56001	2	11.3668634302958118	53.277110195393341	\N	f
56001	79	11.4668388620057442	53.3603601908764276	\N	f
56001	111	11.5030134805220392	53.400697635129788	\N	f
56001	104	11.5005937181545885	53.3957429989220884	\N	f
56001	77	11.4633732243636235	53.3597050985627988	\N	f
56001	105	11.5019754283571718	53.3980405717650157	\N	f
56001	10	11.3663169301968132	53.2862775762292173	\N	f
56001	22	11.3747675651632445	53.3081286394922884	\N	f
56001	80	11.4677370295167798	53.3605015656464161	\N	f
56001	110	11.5030790456327576	53.4003658980212919	\N	f
56001	86	11.4731929035453408	53.3594795322301891	\N	f
56001	53	11.4255568703761003	53.3494385707997978	\N	f
56001	33	11.3910042432920093	53.3310145884234004	\N	f
56001	98	11.492782529282179	53.3692813300180759	\N	f
56001	60	11.4420364374377534	53.3534346897922731	\N	f
56001	14	11.3658294759503633	53.2957770667026267	\N	f
56001	48	11.4103845077093951	53.3436043935329707	\N	f
56001	46	11.4082055853651223	53.344018087029859	\N	f
56001	11	11.3661854274463376	53.2888593887282767	\N	f
56001	89	11.4835570339163482	53.3588307729102098	\N	f
56001	44	11.4075396897093881	53.3440726625339536	\N	f
56001	54	11.4276730215774407	53.3505228165653946	\N	f
56001	39	11.404349723555514	53.3416935058316639	\N	f
56001	66	11.4467701639258159	53.355085924753979	\N	f
56001	19	11.3723826342608625	53.3038028323010238	\N	f
56001	20	11.3731329077437433	53.305191248026297	\N	f
56001	31	11.3876367670514451	53.3291055633672855	\N	f
56001	84	11.4708519310694612	53.3601357421309288	\N	f
56001	7	11.3666896454995054	53.2809176284279857	\N	f
56001	92	11.4885565598731461	53.3581477409187173	\N	f
56001	42	11.406470903898871	53.3436977120570361	\N	f
56001	90	11.486310768566522	53.3586301660231541	\N	f
56001	114	11.5011450611310835	53.4050720573605346	\N	f
56001	35	11.3947306512608524	53.3323100581536451	\N	f
56001	57	11.4316508866470503	53.3516415212670339	\N	f
56001	62	11.4449837009232578	53.3540961150995514	\N	f
56001	74	11.4523132097493079	53.3593722438671918	\N	f
56001	97	11.4922609886287379	53.3667406819777312	\N	f
56001	67	11.4471706326418516	53.3554709335149298	\N	f
56001	59	11.4408866265614328	53.3532530818861659	\N	f
56001	49	11.4115488472182047	53.3433557303999919	\N	f
56001	95	11.4914300626090924	53.3628423518577719	\N	f
56001	68	11.4479296605571292	53.3564456557433076	\N	f
56001	1	11.3668597050054299	53.2768091919304965	\N	f
56001	30	11.386493289168774	53.3283707498394861	\N	f
56001	116	11.5001327134698492	53.4080377610334267	\N	f
56001	15	11.3662007011369024	53.2969300440757721	\N	f
56001	3	11.3668621264441789	53.2774149241465693	\N	f
56001	6	11.3667265258742844	53.2802837702695413	\N	f
56001	64	11.4457503656838178	53.3544299011177543	\N	f
56001	72	11.4503570597698623	53.358840831194243	\N	f
56001	51	11.413180338140883	53.3427071573445346	\N	f
56001	50	11.4124784934329657	53.3430893721376975	\N	f
56001	93	11.4892433171550188	53.3578916272049781	\N	f
56001	65	11.4462832684729232	53.3547269930257002	\N	f
56001	88	11.4797164457973047	53.3590611821203211	\N	f
56001	82	11.4697490588519511	53.3604240796064744	\N	f
56001	38	11.4021251664040655	53.3394978196806733	\N	f
56001	85	11.4710493714596939	53.3600781863945315	\N	f
56001	0	11.3668551553295369	53.2764918887278824	\N	t
56001	107	11.5027465634661894	53.399416880296549	\N	f
56001	78	11.4643689944826583	53.3598485222425012	\N	f
56001	9	11.3663862205979136	53.2847749803537525	\N	f
56001	75	11.4534518447544844	53.3594795322301891	\N	f
56001	37	11.3976618959977163	53.3350390196227835	\N	f
56001	23	11.3736119800868334	53.3083005616434065	\N	f
56001	43	11.4068983809701745	53.3438806238147762	\N	f
56001	71	11.4497189175274734	53.3585068589115181	\N	f
56001	58	11.4369724639573498	53.3526151259082937	\N	f
56001	96	11.491843569841464	53.3647046245196037	\N	f
56001	115	11.5009068288111731	53.4059102476964185	\N	f
56001	94	11.4903014858879473	53.3576314156718112	\N	f
56001	108	11.5030032359734893	53.3998456612194872	\N	f
56001	119	11.4971598281581588	53.4090213635754907	\N	t
56001	47	11.4092112275036701	53.3438225092848199	\N	f
56001	100	11.4970470554466626	53.3892911685099705	\N	f
56001	63	11.4454696650535546	53.3543056626835224	\N	f
56001	102	11.4979098326990723	53.3925997851625027	\N	f
56001	21	11.3740713083908993	53.3068184548650308	\N	f
56001	56	11.4291759899819496	53.3511384208009787	\N	f
56001	5	11.3667831502880858	53.2790801289472	\N	f
56001	13	11.3658399067634317	53.2955755284929751	\N	f
56001	52	11.415943944810568	53.3442123609232652	\N	f
56001	45	11.4081450493964187	53.3440229299073607	\N	f
56001	41	11.4061214716610646	53.3434499802466462	\N	f
56001	101	11.4973564408128652	53.3908770246254747	\N	f
56001	24	11.3761535594497669	53.3121620113885868	\N	f
55952	47	11.4092112275036701	53.3438225092848199	\N	f
55952	80	11.4677370295167798	53.3605015656464161	\N	f
55952	21	11.3740713083908993	53.3068184548650308	\N	f
55952	99	11.494642194240738	53.378353715949217	\N	f
55952	59	11.4408866265614328	53.3532530818861659	\N	f
55952	112	11.5028480776290909	53.4010761246325671	\N	f
55952	93	11.4892433171550188	53.3578916272049781	\N	f
55952	11	11.3661854274463376	53.2888593887282767	\N	f
55952	22	11.3747675651632445	53.3081286394922884	\N	f
55952	18	11.3683557816227321	53.3043018349476583	\N	f
55952	113	11.5017889775735664	53.4028037280470969	\N	f
55952	98	11.492782529282179	53.3692813300180759	\N	f
55952	57	11.4316508866470503	53.3516415212670339	\N	f
55952	8	11.3666140221047556	53.2822030398742044	\N	f
55952	78	11.4643689944826583	53.3598485222425012	\N	f
55952	71	11.4497189175274734	53.3585068589115181	\N	f
55952	118	11.4981992877617323	53.4091007726438534	\N	f
55952	95	11.4914300626090924	53.3628423518577719	\N	f
55952	26	11.3827156584571814	53.3225242791144041	\N	f
55952	77	11.4633732243636235	53.3597050985627988	\N	f
55952	76	11.4621861605834852	53.3596225833808475	\N	f
55952	4	11.3668269224500715	53.2781605410164687	\N	f
55952	83	11.4706516967114442	53.3601867786091617	\N	f
55952	10	11.3663169301968132	53.2862775762292173	\N	f
55952	23	11.3736119800868334	53.3083005616434065	\N	f
55952	46	11.4082055853651223	53.344018087029859	\N	f
55952	74	11.4523132097493079	53.3593722438671918	\N	f
55952	25	11.3816856156666351	53.3209203553405473	\N	f
55952	55	11.4286229706247813	53.3509664986498606	\N	f
55952	86	11.4731929035453408	53.3594795322301891	\N	f
55952	111	11.5030134805220392	53.400697635129788	\N	f
55952	110	11.5030790456327576	53.4003658980212919	\N	f
55952	30	11.386493289168774	53.3283707498394861	\N	f
55952	115	11.5009068288111731	53.4059102476964185	\N	f
55952	119	11.4971598281581588	53.4090213635754907	\N	t
55952	9	11.3663862205979136	53.2847749803537525	\N	f
55952	117	11.4998847953949443	53.409178817477347	\N	f
55952	114	11.5011450611310835	53.4050720573605346	\N	f
55952	14	11.3658294759503633	53.2957770667026267	\N	f
55952	61	11.4441822046976291	53.3538446579987848	\N	f
55952	64	11.4457503656838178	53.3544299011177543	\N	f
55952	90	11.486310768566522	53.3586301660231541	\N	f
55952	17	11.3683809273328098	53.3039624609938798	\N	f
55952	15	11.3662007011369024	53.2969300440757721	\N	f
55952	20	11.3731329077437433	53.305191248026297	\N	f
55952	81	11.4687290743454344	53.3605332306146565	\N	f
55952	43	11.4068983809701745	53.3438806238147762	\N	f
55952	33	11.3910042432920093	53.3310145884234004	\N	f
55952	0	11.3668551553295369	53.2764918887278824	\N	t
55953	104	11.5005937181545885	53.3957429989220884	\N	f
55953	61	11.4441822046976291	53.3538446579987848	\N	f
55953	119	11.4971598281581588	53.4090213635754907	\N	t
55953	60	11.4420364374377534	53.3534346897922731	\N	f
55953	91	11.4874980186111788	53.3584722137109679	\N	f
55953	66	11.4467701639258159	53.355085924753979	\N	f
55953	81	11.4687290743454344	53.3605332306146565	\N	f
55953	45	11.4081450493964187	53.3440229299073607	\N	f
55953	90	11.486310768566522	53.3586301660231541	\N	f
55953	106	11.5026904978459434	53.39931816010143	\N	f
55953	115	11.5009068288111731	53.4059102476964185	\N	f
55953	82	11.4697490588519511	53.3604240796064744	\N	f
55953	31	11.3876367670514451	53.3291055633672855	\N	f
55953	78	11.4643689944826583	53.3598485222425012	\N	f
55953	39	11.404349723555514	53.3416935058316639	\N	f
55953	2	11.3668634302958118	53.277110195393341	\N	f
55953	93	11.4892433171550188	53.3578916272049781	\N	f
55953	17	11.3683809273328098	53.3039624609938798	\N	f
55953	105	11.5019754283571718	53.3980405717650157	\N	f
55953	74	11.4523132097493079	53.3593722438671918	\N	f
55953	16	11.3679217852932606	53.3022754632445128	\N	f
55953	9	11.3663862205979136	53.2847749803537525	\N	f
55953	28	11.3853159111436284	53.326561935094638	\N	f
55953	107	11.5027465634661894	53.399416880296549	\N	f
55953	56	11.4291759899819496	53.3511384208009787	\N	f
55953	4	11.3668269224500715	53.2781605410164687	\N	f
55953	18	11.3683557816227321	53.3043018349476583	\N	f
55953	112	11.5028480776290909	53.4010761246325671	\N	f
55953	30	11.386493289168774	53.3283707498394861	\N	f
55953	21	11.3740713083908993	53.3068184548650308	\N	f
55953	77	11.4633732243636235	53.3597050985627988	\N	f
55953	35	11.3947306512608524	53.3323100581536451	\N	f
55953	47	11.4092112275036701	53.3438225092848199	\N	f
55953	86	11.4731929035453408	53.3594795322301891	\N	f
55953	76	11.4621861605834852	53.3596225833808475	\N	f
55953	20	11.3731329077437433	53.305191248026297	\N	f
55953	36	11.3951650201193626	53.3324909210016784	\N	f
55953	8	11.3666140221047556	53.2822030398742044	\N	f
55953	67	11.4471706326418516	53.3554709335149298	\N	f
55953	59	11.4408866265614328	53.3532530818861659	\N	f
55953	118	11.4981992877617323	53.4091007726438534	\N	f
55953	99	11.494642194240738	53.378353715949217	\N	f
55953	69	11.448233457987758	53.3568584179176071	\N	f
55953	26	11.3827156584571814	53.3225242791144041	\N	f
55953	80	11.4677370295167798	53.3605015656464161	\N	f
55953	3	11.3668621264441789	53.2774149241465693	\N	f
55953	24	11.3761535594497669	53.3121620113885868	\N	f
55953	65	11.4462832684729232	53.3547269930257002	\N	f
55953	108	11.5030032359734893	53.3998456612194872	\N	f
55953	22	11.3747675651632445	53.3081286394922884	\N	f
55953	10	11.3663169301968132	53.2862775762292173	\N	f
55953	102	11.4979098326990723	53.3925997851625027	\N	f
55953	88	11.4797164457973047	53.3590611821203211	\N	f
55953	64	11.4457503656838178	53.3544299011177543	\N	f
55953	95	11.4914300626090924	53.3628423518577719	\N	f
55953	38	11.4021251664040655	53.3394978196806733	\N	f
55953	72	11.4503570597698623	53.358840831194243	\N	f
55953	98	11.492782529282179	53.3692813300180759	\N	f
55953	89	11.4835570339163482	53.3588307729102098	\N	f
55953	51	11.413180338140883	53.3427071573445346	\N	f
55953	25	11.3816856156666351	53.3209203553405473	\N	f
55953	1	11.3668597050054299	53.2768091919304965	\N	f
55953	44	11.4075396897093881	53.3440726625339536	\N	f
55953	94	11.4903014858879473	53.3576314156718112	\N	f
55953	70	11.4490124162065783	53.3579167729150541	\N	f
55953	41	11.4061214716610646	53.3434499802466462	\N	f
55953	101	11.4973564408128652	53.3908770246254747	\N	f
55953	50	11.4124784934329657	53.3430893721376975	\N	f
55953	113	11.5017889775735664	53.4028037280470969	\N	f
55953	63	11.4454696650535546	53.3543056626835224	\N	f
55953	58	11.4369724639573498	53.3526151259082937	\N	f
55953	54	11.4276730215774407	53.3505228165653946	\N	f
55953	109	11.5030555763033533	53.4000395625838564	\N	f
55953	117	11.4998847953949443	53.409178817477347	\N	f
55953	29	11.3860019233674237	53.3276478572409118	\N	f
55953	11	11.3661854274463376	53.2888593887282767	\N	f
55953	19	11.3723826342608625	53.3038028323010238	\N	f
55953	71	11.4497189175274734	53.3585068589115181	\N	f
55953	12	11.366174437839712	53.2890549664733157	\N	f
55953	96	11.491843569841464	53.3647046245196037	\N	f
55953	52	11.415943944810568	53.3442123609232652	\N	f
55953	110	11.5030790456327576	53.4003658980212919	\N	f
55953	84	11.4708519310694612	53.3601357421309288	\N	f
55953	34	11.3921857191365739	53.3314146846103938	\N	f
55953	75	11.4534518447544844	53.3594795322301891	\N	f
55953	0	11.3668551553295369	53.2764918887278824	\N	t
55953	15	11.3662007011369024	53.2969300440757721	\N	f
55953	49	11.4115488472182047	53.3433557303999919	\N	f
55953	92	11.4885565598731461	53.3581477409187173	\N	f
55953	103	11.4995448626476122	53.3945687873937658	\N	f
55953	23	11.3736119800868334	53.3083005616434065	\N	f
55953	55	11.4286229706247813	53.3509664986498606	\N	f
55953	48	11.4103845077093951	53.3436043935329707	\N	f
55953	97	11.4922609886287379	53.3667406819777312	\N	f
55953	13	11.3658399067634317	53.2955755284929751	\N	f
55953	62	11.4449837009232578	53.3540961150995514	\N	f
55953	116	11.5001327134698492	53.4080377610334267	\N	f
55953	40	11.4053866581332688	53.3427775653327458	\N	f
55953	100	11.4970470554466626	53.3892911685099705	\N	f
55953	83	11.4706516967114442	53.3601867786091617	\N	f
55953	57	11.4316508866470503	53.3516415212670339	\N	f
55953	14	11.3658294759503633	53.2957770667026267	\N	f
55953	43	11.4068983809701745	53.3438806238147762	\N	f
55953	42	11.406470903898871	53.3436977120570361	\N	f
55953	111	11.5030134805220392	53.400697635129788	\N	f
55953	114	11.5011450611310835	53.4050720573605346	\N	f
55953	37	11.3976618959977163	53.3350390196227835	\N	f
55953	46	11.4082055853651223	53.344018087029859	\N	f
55953	79	11.4668388620057442	53.3603601908764276	\N	f
55953	53	11.4255568703761003	53.3494385707997978	\N	f
55953	87	11.4745487229797707	53.3591105422178771	\N	f
55953	27	11.3840211864714593	53.3245972169473177	\N	f
55953	73	11.451422865348075	53.3591833716448392	\N	f
55953	32	11.389851265918864	53.3305286242931018	\N	f
55953	7	11.3666896454995054	53.2809176284279857	\N	f
55953	5	11.3667831502880858	53.2790801289472	\N	f
55953	68	11.4479296605571292	53.3564456557433076	\N	f
55953	6	11.3667265258742844	53.2802837702695413	\N	f
55953	85	11.4710493714596939	53.3600781863945315	\N	f
55953	33	11.3910042432920093	53.3310145884234004	\N	f
55954	15	11.3662007011369024	53.2969300440757721	\N	f
55954	36	11.3951650201193626	53.3324909210016784	\N	f
55954	104	11.5005937181545885	53.3957429989220884	\N	f
55954	101	11.4973564408128652	53.3908770246254747	\N	f
55954	8	11.3666140221047556	53.2822030398742044	\N	f
55954	51	11.413180338140883	53.3427071573445346	\N	f
55954	89	11.4835570339163482	53.3588307729102098	\N	f
55954	90	11.486310768566522	53.3586301660231541	\N	f
55954	65	11.4462832684729232	53.3547269930257002	\N	f
55954	41	11.4061214716610646	53.3434499802466462	\N	f
55954	25	11.3816856156666351	53.3209203553405473	\N	f
55954	39	11.404349723555514	53.3416935058316639	\N	f
55954	80	11.4677370295167798	53.3605015656464161	\N	f
55954	38	11.4021251664040655	53.3394978196806733	\N	f
55954	79	11.4668388620057442	53.3603601908764276	\N	f
55954	28	11.3853159111436284	53.326561935094638	\N	f
55954	114	11.5011450611310835	53.4050720573605346	\N	f
55954	18	11.3683557816227321	53.3043018349476583	\N	f
55954	21	11.3740713083908993	53.3068184548650308	\N	f
55954	45	11.4081450493964187	53.3440229299073607	\N	f
55954	1	11.3668597050054299	53.2768091919304965	\N	f
55954	27	11.3840211864714593	53.3245972169473177	\N	f
55954	110	11.5030790456327576	53.4003658980212919	\N	f
55954	32	11.389851265918864	53.3305286242931018	\N	f
55954	99	11.494642194240738	53.378353715949217	\N	f
55954	73	11.451422865348075	53.3591833716448392	\N	f
55954	52	11.415943944810568	53.3442123609232652	\N	f
55954	46	11.4082055853651223	53.344018087029859	\N	f
55954	48	11.4103845077093951	53.3436043935329707	\N	f
55954	20	11.3731329077437433	53.305191248026297	\N	f
55954	72	11.4503570597698623	53.358840831194243	\N	f
55954	4	11.3668269224500715	53.2781605410164687	\N	f
55954	44	11.4075396897093881	53.3440726625339536	\N	f
55954	103	11.4995448626476122	53.3945687873937658	\N	f
55954	33	11.3910042432920093	53.3310145884234004	\N	f
55954	64	11.4457503656838178	53.3544299011177543	\N	f
55954	3	11.3668621264441789	53.2774149241465693	\N	f
55954	19	11.3723826342608625	53.3038028323010238	\N	f
55954	29	11.3860019233674237	53.3276478572409118	\N	f
55954	43	11.4068983809701745	53.3438806238147762	\N	f
55954	11	11.3661854274463376	53.2888593887282767	\N	f
55954	24	11.3761535594497669	53.3121620113885868	\N	f
55954	60	11.4420364374377534	53.3534346897922731	\N	f
55954	86	11.4731929035453408	53.3594795322301891	\N	f
55954	31	11.3876367670514451	53.3291055633672855	\N	f
55954	84	11.4708519310694612	53.3601357421309288	\N	f
55954	57	11.4316508866470503	53.3516415212670339	\N	f
55954	111	11.5030134805220392	53.400697635129788	\N	f
55954	5	11.3667831502880858	53.2790801289472	\N	f
55954	7	11.3666896454995054	53.2809176284279857	\N	f
55954	77	11.4633732243636235	53.3597050985627988	\N	f
55954	93	11.4892433171550188	53.3578916272049781	\N	f
55954	56	11.4291759899819496	53.3511384208009787	\N	f
55954	78	11.4643689944826583	53.3598485222425012	\N	f
55954	16	11.3679217852932606	53.3022754632445128	\N	f
55954	68	11.4479296605571292	53.3564456557433076	\N	f
55954	40	11.4053866581332688	53.3427775653327458	\N	f
55954	102	11.4979098326990723	53.3925997851625027	\N	f
55954	71	11.4497189175274734	53.3585068589115181	\N	f
55954	74	11.4523132097493079	53.3593722438671918	\N	f
55954	117	11.4998847953949443	53.409178817477347	\N	f
55954	35	11.3947306512608524	53.3323100581536451	\N	f
55954	97	11.4922609886287379	53.3667406819777312	\N	f
55954	107	11.5027465634661894	53.399416880296549	\N	f
55954	81	11.4687290743454344	53.3605332306146565	\N	f
55954	88	11.4797164457973047	53.3590611821203211	\N	f
55954	47	11.4092112275036701	53.3438225092848199	\N	f
55954	96	11.491843569841464	53.3647046245196037	\N	f
55954	10	11.3663169301968132	53.2862775762292173	\N	f
55954	66	11.4467701639258159	53.355085924753979	\N	f
55954	55	11.4286229706247813	53.3509664986498606	\N	f
55954	118	11.4981992877617323	53.4091007726438534	\N	f
55954	91	11.4874980186111788	53.3584722137109679	\N	f
55954	12	11.366174437839712	53.2890549664733157	\N	f
55954	69	11.448233457987758	53.3568584179176071	\N	f
55954	83	11.4706516967114442	53.3601867786091617	\N	f
55954	75	11.4534518447544844	53.3594795322301891	\N	f
55954	92	11.4885565598731461	53.3581477409187173	\N	f
55954	59	11.4408866265614328	53.3532530818861659	\N	f
55954	63	11.4454696650535546	53.3543056626835224	\N	f
55954	23	11.3736119800868334	53.3083005616434065	\N	f
55954	50	11.4124784934329657	53.3430893721376975	\N	f
55954	22	11.3747675651632445	53.3081286394922884	\N	f
55954	37	11.3976618959977163	53.3350390196227835	\N	f
55954	70	11.4490124162065783	53.3579167729150541	\N	f
55954	116	11.5001327134698492	53.4080377610334267	\N	f
55954	30	11.386493289168774	53.3283707498394861	\N	f
55954	26	11.3827156584571814	53.3225242791144041	\N	f
55954	76	11.4621861605834852	53.3596225833808475	\N	f
55954	62	11.4449837009232578	53.3540961150995514	\N	f
55954	113	11.5017889775735664	53.4028037280470969	\N	f
55954	9	11.3663862205979136	53.2847749803537525	\N	f
55954	105	11.5019754283571718	53.3980405717650157	\N	f
55954	100	11.4970470554466626	53.3892911685099705	\N	f
55954	2	11.3668634302958118	53.277110195393341	\N	f
55954	53	11.4255568703761003	53.3494385707997978	\N	f
55954	6	11.3667265258742844	53.2802837702695413	\N	f
55954	0	11.3668551553295369	53.2764918887278824	\N	t
55954	13	11.3658399067634317	53.2955755284929751	\N	f
55954	109	11.5030555763033533	53.4000395625838564	\N	f
55954	108	11.5030032359734893	53.3998456612194872	\N	f
55954	94	11.4903014858879473	53.3576314156718112	\N	f
55954	112	11.5028480776290909	53.4010761246325671	\N	f
55954	85	11.4710493714596939	53.3600781863945315	\N	f
55954	95	11.4914300626090924	53.3628423518577719	\N	f
55954	119	11.4971598281581588	53.4090213635754907	\N	t
55954	14	11.3658294759503633	53.2957770667026267	\N	f
55954	49	11.4115488472182047	53.3433557303999919	\N	f
55954	61	11.4441822046976291	53.3538446579987848	\N	f
55954	82	11.4697490588519511	53.3604240796064744	\N	f
55954	115	11.5009068288111731	53.4059102476964185	\N	f
55954	87	11.4745487229797707	53.3591105422178771	\N	f
55954	54	11.4276730215774407	53.3505228165653946	\N	f
55954	42	11.406470903898871	53.3436977120570361	\N	f
55954	67	11.4471706326418516	53.3554709335149298	\N	f
55954	98	11.492782529282179	53.3692813300180759	\N	f
55954	17	11.3683809273328098	53.3039624609938798	\N	f
55954	106	11.5026904978459434	53.39931816010143	\N	f
55954	58	11.4369724639573498	53.3526151259082937	\N	f
55954	34	11.3921857191365739	53.3314146846103938	\N	f
55955	21	11.3740713083908993	53.3068184548650308	\N	f
55955	23	11.3736119800868334	53.3083005616434065	\N	f
55955	11	11.3661854274463376	53.2888593887282767	\N	f
55955	117	11.4998847953949443	53.409178817477347	\N	f
55955	15	11.3662007011369024	53.2969300440757721	\N	f
55955	42	11.406470903898871	53.3436977120570361	\N	f
55955	48	11.4103845077093951	53.3436043935329707	\N	f
55955	34	11.3921857191365739	53.3314146846103938	\N	f
55955	108	11.5030032359734893	53.3998456612194872	\N	f
55955	68	11.4479296605571292	53.3564456557433076	\N	f
55955	30	11.386493289168774	53.3283707498394861	\N	f
55955	97	11.4922609886287379	53.3667406819777312	\N	f
55955	96	11.491843569841464	53.3647046245196037	\N	f
55955	95	11.4914300626090924	53.3628423518577719	\N	f
55955	18	11.3683557816227321	53.3043018349476583	\N	f
55955	86	11.4731929035453408	53.3594795322301891	\N	f
55955	94	11.4903014858879473	53.3576314156718112	\N	f
55955	92	11.4885565598731461	53.3581477409187173	\N	f
55955	43	11.4068983809701745	53.3438806238147762	\N	f
55955	0	11.3668551553295369	53.2764918887278824	\N	t
55955	51	11.413180338140883	53.3427071573445346	\N	f
55955	80	11.4677370295167798	53.3605015656464161	\N	f
55955	36	11.3951650201193626	53.3324909210016784	\N	f
55955	112	11.5028480776290909	53.4010761246325671	\N	f
55955	104	11.5005937181545885	53.3957429989220884	\N	f
55955	89	11.4835570339163482	53.3588307729102098	\N	f
55955	63	11.4454696650535546	53.3543056626835224	\N	f
55955	87	11.4745487229797707	53.3591105422178771	\N	f
55955	61	11.4441822046976291	53.3538446579987848	\N	f
55955	29	11.3860019233674237	53.3276478572409118	\N	f
55955	22	11.3747675651632445	53.3081286394922884	\N	f
55955	20	11.3731329077437433	53.305191248026297	\N	f
55955	109	11.5030555763033533	53.4000395625838564	\N	f
55955	53	11.4255568703761003	53.3494385707997978	\N	f
55955	41	11.4061214716610646	53.3434499802466462	\N	f
55955	4	11.3668269224500715	53.2781605410164687	\N	f
55955	84	11.4708519310694612	53.3601357421309288	\N	f
55955	106	11.5026904978459434	53.39931816010143	\N	f
55955	47	11.4092112275036701	53.3438225092848199	\N	f
55955	32	11.389851265918864	53.3305286242931018	\N	f
55955	66	11.4467701639258159	53.355085924753979	\N	f
55955	62	11.4449837009232578	53.3540961150995514	\N	f
55955	2	11.3668634302958118	53.277110195393341	\N	f
55955	40	11.4053866581332688	53.3427775653327458	\N	f
55955	118	11.4981992877617323	53.4091007726438534	\N	f
55955	78	11.4643689944826583	53.3598485222425012	\N	f
55955	93	11.4892433171550188	53.3578916272049781	\N	f
55955	77	11.4633732243636235	53.3597050985627988	\N	f
55955	3	11.3668621264441789	53.2774149241465693	\N	f
55955	90	11.486310768566522	53.3586301660231541	\N	f
55955	38	11.4021251664040655	53.3394978196806733	\N	f
55955	12	11.366174437839712	53.2890549664733157	\N	f
55955	103	11.4995448626476122	53.3945687873937658	\N	f
55955	83	11.4706516967114442	53.3601867786091617	\N	f
55955	102	11.4979098326990723	53.3925997851625027	\N	f
55955	91	11.4874980186111788	53.3584722137109679	\N	f
55955	50	11.4124784934329657	53.3430893721376975	\N	f
55955	69	11.448233457987758	53.3568584179176071	\N	f
55955	75	11.4534518447544844	53.3594795322301891	\N	f
55955	6	11.3667265258742844	53.2802837702695413	\N	f
55955	19	11.3723826342608625	53.3038028323010238	\N	f
55955	9	11.3663862205979136	53.2847749803537525	\N	f
55955	72	11.4503570597698623	53.358840831194243	\N	f
55955	98	11.492782529282179	53.3692813300180759	\N	f
55955	79	11.4668388620057442	53.3603601908764276	\N	f
55955	46	11.4082055853651223	53.344018087029859	\N	f
55955	101	11.4973564408128652	53.3908770246254747	\N	f
55955	13	11.3658399067634317	53.2955755284929751	\N	f
55955	16	11.3679217852932606	53.3022754632445128	\N	f
55955	28	11.3853159111436284	53.326561935094638	\N	f
55955	27	11.3840211864714593	53.3245972169473177	\N	f
55955	59	11.4408866265614328	53.3532530818861659	\N	f
55955	114	11.5011450611310835	53.4050720573605346	\N	f
55955	67	11.4471706326418516	53.3554709335149298	\N	f
55955	88	11.4797164457973047	53.3590611821203211	\N	f
55955	85	11.4710493714596939	53.3600781863945315	\N	f
55955	115	11.5009068288111731	53.4059102476964185	\N	f
55955	81	11.4687290743454344	53.3605332306146565	\N	f
55955	17	11.3683809273328098	53.3039624609938798	\N	f
55955	82	11.4697490588519511	53.3604240796064744	\N	f
55955	5	11.3667831502880858	53.2790801289472	\N	f
55955	74	11.4523132097493079	53.3593722438671918	\N	f
55955	45	11.4081450493964187	53.3440229299073607	\N	f
55955	73	11.451422865348075	53.3591833716448392	\N	f
55955	110	11.5030790456327576	53.4003658980212919	\N	f
55955	1	11.3668597050054299	53.2768091919304965	\N	f
55955	8	11.3666140221047556	53.2822030398742044	\N	f
55955	119	11.4971598281581588	53.4090213635754907	\N	t
55955	111	11.5030134805220392	53.400697635129788	\N	f
55955	49	11.4115488472182047	53.3433557303999919	\N	f
55955	100	11.4970470554466626	53.3892911685099705	\N	f
55955	52	11.415943944810568	53.3442123609232652	\N	f
55955	105	11.5019754283571718	53.3980405717650157	\N	f
55955	55	11.4286229706247813	53.3509664986498606	\N	f
55955	35	11.3947306512608524	53.3323100581536451	\N	f
55955	71	11.4497189175274734	53.3585068589115181	\N	f
55955	44	11.4075396897093881	53.3440726625339536	\N	f
55955	7	11.3666896454995054	53.2809176284279857	\N	f
55955	33	11.3910042432920093	53.3310145884234004	\N	f
55955	99	11.494642194240738	53.378353715949217	\N	f
55955	58	11.4369724639573498	53.3526151259082937	\N	f
55955	57	11.4316508866470503	53.3516415212670339	\N	f
55955	24	11.3761535594497669	53.3121620113885868	\N	f
55955	31	11.3876367670514451	53.3291055633672855	\N	f
55955	37	11.3976618959977163	53.3350390196227835	\N	f
55955	64	11.4457503656838178	53.3544299011177543	\N	f
55955	39	11.404349723555514	53.3416935058316639	\N	f
55955	25	11.3816856156666351	53.3209203553405473	\N	f
55955	10	11.3663169301968132	53.2862775762292173	\N	f
55955	76	11.4621861605834852	53.3596225833808475	\N	f
55955	116	11.5001327134698492	53.4080377610334267	\N	f
55955	60	11.4420364374377534	53.3534346897922731	\N	f
55955	54	11.4276730215774407	53.3505228165653946	\N	f
55955	14	11.3658294759503633	53.2957770667026267	\N	f
55955	65	11.4462832684729232	53.3547269930257002	\N	f
55955	56	11.4291759899819496	53.3511384208009787	\N	f
55955	107	11.5027465634661894	53.399416880296549	\N	f
55955	26	11.3827156584571814	53.3225242791144041	\N	f
55955	113	11.5017889775735664	53.4028037280470969	\N	f
55955	70	11.4490124162065783	53.3579167729150541	\N	f
56051	60	11.4420364374377534	53.3534346897922731	\N	f
56051	21	11.3740713083908993	53.3068184548650308	\N	f
56051	113	11.5017889775735664	53.4028037280470969	\N	f
56051	44	11.4075396897093881	53.3440726625339536	\N	f
56051	73	11.451422865348075	53.3591833716448392	\N	f
56051	33	11.3910042432920093	53.3310145884234004	\N	f
56051	106	11.5026904978459434	53.39931816010143	\N	f
56051	14	11.3658294759503633	53.2957770667026267	\N	f
56051	87	11.4745487229797707	53.3591105422178771	\N	f
56051	49	11.4115488472182047	53.3433557303999919	\N	f
56051	16	11.3679217852932606	53.3022754632445128	\N	f
56051	86	11.4731929035453408	53.3594795322301891	\N	f
56051	22	11.3747675651632445	53.3081286394922884	\N	f
56051	43	11.4068983809701745	53.3438806238147762	\N	f
56051	104	11.5005937181545885	53.3957429989220884	\N	f
56051	50	11.4124784934329657	53.3430893721376975	\N	f
56051	37	11.3976618959977163	53.3350390196227835	\N	f
56051	66	11.4467701639258159	53.355085924753979	\N	f
56051	54	11.4276730215774407	53.3505228165653946	\N	f
56051	4	11.3668269224500715	53.2781605410164687	\N	f
56051	30	11.386493289168774	53.3283707498394861	\N	f
56051	80	11.4677370295167798	53.3605015656464161	\N	f
56051	64	11.4457503656838178	53.3544299011177543	\N	f
56051	96	11.491843569841464	53.3647046245196037	\N	f
56051	111	11.5030134805220392	53.400697635129788	\N	f
56051	52	11.415943944810568	53.3442123609232652	\N	f
56051	97	11.4922609886287379	53.3667406819777312	\N	f
56051	47	11.4092112275036701	53.3438225092848199	\N	f
56051	38	11.4021251664040655	53.3394978196806733	\N	f
56051	83	11.4706516967114442	53.3601867786091617	\N	f
56051	18	11.3683557816227321	53.3043018349476583	\N	f
56051	15	11.3662007011369024	53.2969300440757721	\N	f
56051	29	11.3860019233674237	53.3276478572409118	\N	f
56051	84	11.4708519310694612	53.3601357421309288	\N	f
56051	71	11.4497189175274734	53.3585068589115181	\N	f
56051	3	11.3668621264441789	53.2774149241465693	\N	f
56051	90	11.486310768566522	53.3586301660231541	\N	f
56051	25	11.3816856156666351	53.3209203553405473	\N	f
56051	19	11.3723826342608625	53.3038028323010238	\N	f
56051	59	11.4408866265614328	53.3532530818861659	\N	f
56051	10	11.3663169301968132	53.2862775762292173	\N	f
56051	70	11.4490124162065783	53.3579167729150541	\N	f
56051	24	11.3761535594497669	53.3121620113885868	\N	f
56051	101	11.4973564408128652	53.3908770246254747	\N	f
56051	102	11.4979098326990723	53.3925997851625027	\N	f
56051	77	11.4633732243636235	53.3597050985627988	\N	f
56051	92	11.4885565598731461	53.3581477409187173	\N	f
56051	75	11.4534518447544844	53.3594795322301891	\N	f
56051	46	11.4082055853651223	53.344018087029859	\N	f
56051	39	11.404349723555514	53.3416935058316639	\N	f
56051	67	11.4471706326418516	53.3554709335149298	\N	f
56051	41	11.4061214716610646	53.3434499802466462	\N	f
56051	105	11.5019754283571718	53.3980405717650157	\N	f
56051	55	11.4286229706247813	53.3509664986498606	\N	f
56051	114	11.5011450611310835	53.4050720573605346	\N	f
56051	94	11.4903014858879473	53.3576314156718112	\N	f
56051	42	11.406470903898871	53.3436977120570361	\N	f
56051	8	11.3666140221047556	53.2822030398742044	\N	f
56051	13	11.3658399067634317	53.2955755284929751	\N	f
56051	109	11.5030555763033533	53.4000395625838564	\N	f
56051	89	11.4835570339163482	53.3588307729102098	\N	f
56051	110	11.5030790456327576	53.4003658980212919	\N	f
56051	61	11.4441822046976291	53.3538446579987848	\N	f
56051	107	11.5027465634661894	53.399416880296549	\N	f
56051	9	11.3663862205979136	53.2847749803537525	\N	f
56051	53	11.4255568703761003	53.3494385707997978	\N	f
56051	26	11.3827156584571814	53.3225242791144041	\N	f
56051	117	11.4998847953949443	53.409178817477347	\N	f
56051	93	11.4892433171550188	53.3578916272049781	\N	f
56051	119	11.4971598281581588	53.4090213635754907	\N	t
56051	48	11.4103845077093951	53.3436043935329707	\N	f
56051	32	11.389851265918864	53.3305286242931018	\N	f
56051	76	11.4621861605834852	53.3596225833808475	\N	f
56051	5	11.3667831502880858	53.2790801289472	\N	f
56051	51	11.413180338140883	53.3427071573445346	\N	f
56051	17	11.3683809273328098	53.3039624609938798	\N	f
56051	91	11.4874980186111788	53.3584722137109679	\N	f
56051	85	11.4710493714596939	53.3600781863945315	\N	f
56051	31	11.3876367670514451	53.3291055633672855	\N	f
56051	36	11.3951650201193626	53.3324909210016784	\N	f
56051	40	11.4053866581332688	53.3427775653327458	\N	f
56051	81	11.4687290743454344	53.3605332306146565	\N	f
56051	0	11.3668551553295369	53.2764918887278824	\N	t
56051	1	11.3668597050054299	53.2768091919304965	\N	f
56051	27	11.3840211864714593	53.3245972169473177	\N	f
56051	56	11.4291759899819496	53.3511384208009787	\N	f
56051	23	11.3736119800868334	53.3083005616434065	\N	f
56051	74	11.4523132097493079	53.3593722438671918	\N	f
56051	68	11.4479296605571292	53.3564456557433076	\N	f
56051	28	11.3853159111436284	53.326561935094638	\N	f
56051	95	11.4914300626090924	53.3628423518577719	\N	f
56051	72	11.4503570597698623	53.358840831194243	\N	f
56051	116	11.5001327134698492	53.4080377610334267	\N	f
56051	112	11.5028480776290909	53.4010761246325671	\N	f
56051	6	11.3667265258742844	53.2802837702695413	\N	f
56051	45	11.4081450493964187	53.3440229299073607	\N	f
56051	103	11.4995448626476122	53.3945687873937658	\N	f
56051	78	11.4643689944826583	53.3598485222425012	\N	f
56051	2	11.3668634302958118	53.277110195393341	\N	f
56051	88	11.4797164457973047	53.3590611821203211	\N	f
56051	62	11.4449837009232578	53.3540961150995514	\N	f
56051	115	11.5009068288111731	53.4059102476964185	\N	f
56051	65	11.4462832684729232	53.3547269930257002	\N	f
56051	100	11.4970470554466626	53.3892911685099705	\N	f
56051	98	11.492782529282179	53.3692813300180759	\N	f
56051	108	11.5030032359734893	53.3998456612194872	\N	f
56051	63	11.4454696650535546	53.3543056626835224	\N	f
56051	7	11.3666896454995054	53.2809176284279857	\N	f
56051	12	11.366174437839712	53.2890549664733157	\N	f
56051	57	11.4316508866470503	53.3516415212670339	\N	f
56051	20	11.3731329077437433	53.305191248026297	\N	f
56051	35	11.3947306512608524	53.3323100581536451	\N	f
56051	34	11.3921857191365739	53.3314146846103938	\N	f
56051	99	11.494642194240738	53.378353715949217	\N	f
56051	69	11.448233457987758	53.3568584179176071	\N	f
56051	11	11.3661854274463376	53.2888593887282767	\N	f
56051	118	11.4981992877617323	53.4091007726438534	\N	f
56051	82	11.4697490588519511	53.3604240796064744	\N	f
56051	79	11.4668388620057442	53.3603601908764276	\N	f
56051	58	11.4369724639573498	53.3526151259082937	\N	f
56052	114	11.5011450611310835	53.4050720573605346	\N	f
56052	70	11.4490124162065783	53.3579167729150541	\N	f
56052	44	11.4075396897093881	53.3440726625339536	\N	f
56052	3	11.3668621264441789	53.2774149241465693	\N	f
56052	15	11.3662007011369024	53.2969300440757721	\N	f
56052	19	11.3723826342608625	53.3038028323010238	\N	f
56052	116	11.5001327134698492	53.4080377610334267	\N	f
56052	108	11.5030032359734893	53.3998456612194872	\N	f
56052	79	11.4668388620057442	53.3603601908764276	\N	f
56052	100	11.4970470554466626	53.3892911685099705	\N	f
56052	25	11.3816856156666351	53.3209203553405473	\N	f
56052	11	11.3661854274463376	53.2888593887282767	\N	f
56052	43	11.4068983809701745	53.3438806238147762	\N	f
56052	94	11.4903014858879473	53.3576314156718112	\N	f
56052	78	11.4643689944826583	53.3598485222425012	\N	f
56052	64	11.4457503656838178	53.3544299011177543	\N	f
56052	63	11.4454696650535546	53.3543056626835224	\N	f
56052	7	11.3666896454995054	53.2809176284279857	\N	f
56052	52	11.415943944810568	53.3442123609232652	\N	f
56052	1	11.3668597050054299	53.2768091919304965	\N	f
56052	28	11.3853159111436284	53.326561935094638	\N	f
56052	17	11.3683809273328098	53.3039624609938798	\N	f
56052	84	11.4708519310694612	53.3601357421309288	\N	f
56052	118	11.4981992877617323	53.4091007726438534	\N	f
56052	59	11.4408866265614328	53.3532530818861659	\N	f
56052	117	11.4998847953949443	53.409178817477347	\N	f
56052	119	11.4971598281581588	53.4090213635754907	\N	t
56052	75	11.4534518447544844	53.3594795322301891	\N	f
56052	82	11.4697490588519511	53.3604240796064744	\N	f
56052	2	11.3668634302958118	53.277110195393341	\N	f
56052	86	11.4731929035453408	53.3594795322301891	\N	f
56052	6	11.3667265258742844	53.2802837702695413	\N	f
56052	87	11.4745487229797707	53.3591105422178771	\N	f
56052	83	11.4706516967114442	53.3601867786091617	\N	f
56052	57	11.4316508866470503	53.3516415212670339	\N	f
56052	26	11.3827156584571814	53.3225242791144041	\N	f
56052	95	11.4914300626090924	53.3628423518577719	\N	f
56052	41	11.4061214716610646	53.3434499802466462	\N	f
56052	67	11.4471706326418516	53.3554709335149298	\N	f
56052	37	11.3976618959977163	53.3350390196227835	\N	f
56052	42	11.406470903898871	53.3436977120570361	\N	f
56052	40	11.4053866581332688	53.3427775653327458	\N	f
56052	58	11.4369724639573498	53.3526151259082937	\N	f
56052	50	11.4124784934329657	53.3430893721376975	\N	f
56052	104	11.5005937181545885	53.3957429989220884	\N	f
56052	46	11.4082055853651223	53.344018087029859	\N	f
56052	31	11.3876367670514451	53.3291055633672855	\N	f
56052	27	11.3840211864714593	53.3245972169473177	\N	f
56052	10	11.3663169301968132	53.2862775762292173	\N	f
56052	53	11.4255568703761003	53.3494385707997978	\N	f
56052	54	11.4276730215774407	53.3505228165653946	\N	f
56052	35	11.3947306512608524	53.3323100581536451	\N	f
56052	61	11.4441822046976291	53.3538446579987848	\N	f
56052	93	11.4892433171550188	53.3578916272049781	\N	f
56052	96	11.491843569841464	53.3647046245196037	\N	f
56052	115	11.5009068288111731	53.4059102476964185	\N	f
56052	109	11.5030555763033533	53.4000395625838564	\N	f
56052	99	11.494642194240738	53.378353715949217	\N	f
56052	32	11.389851265918864	53.3305286242931018	\N	f
56052	9	11.3663862205979136	53.2847749803537525	\N	f
56052	80	11.4677370295167798	53.3605015656464161	\N	f
56052	90	11.486310768566522	53.3586301660231541	\N	f
56052	51	11.413180338140883	53.3427071573445346	\N	f
56052	56	11.4291759899819496	53.3511384208009787	\N	f
56052	18	11.3683557816227321	53.3043018349476583	\N	f
56052	69	11.448233457987758	53.3568584179176071	\N	f
56052	65	11.4462832684729232	53.3547269930257002	\N	f
56052	36	11.3951650201193626	53.3324909210016784	\N	f
56052	101	11.4973564408128652	53.3908770246254747	\N	f
56052	68	11.4479296605571292	53.3564456557433076	\N	f
56052	13	11.3658399067634317	53.2955755284929751	\N	f
56052	103	11.4995448626476122	53.3945687873937658	\N	f
56052	16	11.3679217852932606	53.3022754632445128	\N	f
56052	21	11.3740713083908993	53.3068184548650308	\N	f
56052	105	11.5019754283571718	53.3980405717650157	\N	f
56052	0	11.3668551553295369	53.2764918887278824	\N	t
56052	66	11.4467701639258159	53.355085924753979	\N	f
56052	81	11.4687290743454344	53.3605332306146565	\N	f
56052	62	11.4449837009232578	53.3540961150995514	\N	f
56052	60	11.4420364374377534	53.3534346897922731	\N	f
56052	23	11.3736119800868334	53.3083005616434065	\N	f
56052	77	11.4633732243636235	53.3597050985627988	\N	f
56052	49	11.4115488472182047	53.3433557303999919	\N	f
56052	30	11.386493289168774	53.3283707498394861	\N	f
56052	88	11.4797164457973047	53.3590611821203211	\N	f
56052	33	11.3910042432920093	53.3310145884234004	\N	f
56052	102	11.4979098326990723	53.3925997851625027	\N	f
56052	55	11.4286229706247813	53.3509664986498606	\N	f
56052	48	11.4103845077093951	53.3436043935329707	\N	f
56052	34	11.3921857191365739	53.3314146846103938	\N	f
56052	71	11.4497189175274734	53.3585068589115181	\N	f
56052	24	11.3761535594497669	53.3121620113885868	\N	f
56052	91	11.4874980186111788	53.3584722137109679	\N	f
56052	74	11.4523132097493079	53.3593722438671918	\N	f
56052	113	11.5017889775735664	53.4028037280470969	\N	f
56052	8	11.3666140221047556	53.2822030398742044	\N	f
56052	76	11.4621861605834852	53.3596225833808475	\N	f
56052	98	11.492782529282179	53.3692813300180759	\N	f
56052	112	11.5028480776290909	53.4010761246325671	\N	f
56052	29	11.3860019233674237	53.3276478572409118	\N	f
56052	97	11.4922609886287379	53.3667406819777312	\N	f
56052	107	11.5027465634661894	53.399416880296549	\N	f
56052	111	11.5030134805220392	53.400697635129788	\N	f
56052	45	11.4081450493964187	53.3440229299073607	\N	f
56052	89	11.4835570339163482	53.3588307729102098	\N	f
56052	22	11.3747675651632445	53.3081286394922884	\N	f
56052	47	11.4092112275036701	53.3438225092848199	\N	f
56052	14	11.3658294759503633	53.2957770667026267	\N	f
56052	39	11.404349723555514	53.3416935058316639	\N	f
56052	92	11.4885565598731461	53.3581477409187173	\N	f
56052	85	11.4710493714596939	53.3600781863945315	\N	f
56052	4	11.3668269224500715	53.2781605410164687	\N	f
56052	106	11.5026904978459434	53.39931816010143	\N	f
56052	38	11.4021251664040655	53.3394978196806733	\N	f
56052	73	11.451422865348075	53.3591833716448392	\N	f
56052	12	11.366174437839712	53.2890549664733157	\N	f
56052	110	11.5030790456327576	53.4003658980212919	\N	f
56052	72	11.4503570597698623	53.358840831194243	\N	f
56052	20	11.3731329077437433	53.305191248026297	\N	f
56052	5	11.3667831502880858	53.2790801289472	\N	f
56101	25	11.3816856156666351	53.3209203553405473	\N	f
56101	50	11.4124784934329657	53.3430893721376975	\N	f
56101	67	11.4471706326418516	53.3554709335149298	\N	f
56101	56	11.4291759899819496	53.3511384208009787	\N	f
56101	29	11.3860019233674237	53.3276478572409118	\N	f
56101	24	11.3761535594497669	53.3121620113885868	\N	f
56101	5	11.3667831502880858	53.2790801289472	\N	f
56101	94	11.4903014858879473	53.3576314156718112	\N	f
56101	72	11.4503570597698623	53.358840831194243	\N	f
56101	6	11.3667265258742844	53.2802837702695413	\N	f
56101	47	11.4092112275036701	53.3438225092848199	\N	f
56101	37	11.3976618959977163	53.3350390196227835	\N	f
56101	46	11.4082055853651223	53.344018087029859	\N	f
56101	42	11.406470903898871	53.3436977120570361	\N	f
56101	40	11.4053866581332688	53.3427775653327458	\N	f
56101	9	11.3663862205979136	53.2847749803537525	\N	f
56101	4	11.3668269224500715	53.2781605410164687	\N	f
56101	13	11.3658399067634317	53.2955755284929751	\N	f
56101	16	11.3679217852932606	53.3022754632445128	\N	f
56101	63	11.4454696650535546	53.3543056626835224	\N	f
56101	114	11.5011450611310835	53.4050720573605346	\N	f
56101	44	11.4075396897093881	53.3440726625339536	\N	f
56101	80	11.4677370295167798	53.3605015656464161	\N	f
56101	74	11.4523132097493079	53.3593722438671918	\N	f
56101	48	11.4103845077093951	53.3436043935329707	\N	f
56101	91	11.4874980186111788	53.3584722137109679	\N	f
56101	55	11.4286229706247813	53.3509664986498606	\N	f
56101	33	11.3910042432920093	53.3310145884234004	\N	f
56101	8	11.3666140221047556	53.2822030398742044	\N	f
56101	99	11.494642194240738	53.378353715949217	\N	f
56101	115	11.5009068288111731	53.4059102476964185	\N	f
56101	31	11.3876367670514451	53.3291055633672855	\N	f
56101	81	11.4687290743454344	53.3605332306146565	\N	f
56101	30	11.386493289168774	53.3283707498394861	\N	f
56101	23	11.3736119800868334	53.3083005616434065	\N	f
56101	51	11.413180338140883	53.3427071573445346	\N	f
56101	92	11.4885565598731461	53.3581477409187173	\N	f
56101	73	11.451422865348075	53.3591833716448392	\N	f
56101	58	11.4369724639573498	53.3526151259082937	\N	f
56101	104	11.5005937181545885	53.3957429989220884	\N	f
56101	43	11.4068983809701745	53.3438806238147762	\N	f
56101	11	11.3661854274463376	53.2888593887282767	\N	f
56101	87	11.4745487229797707	53.3591105422178771	\N	f
56101	64	11.4457503656838178	53.3544299011177543	\N	f
56101	26	11.3827156584571814	53.3225242791144041	\N	f
56101	89	11.4835570339163482	53.3588307729102098	\N	f
56101	39	11.404349723555514	53.3416935058316639	\N	f
56101	38	11.4021251664040655	53.3394978196806733	\N	f
56101	90	11.486310768566522	53.3586301660231541	\N	f
56101	86	11.4731929035453408	53.3594795322301891	\N	f
56101	15	11.3662007011369024	53.2969300440757721	\N	f
56101	107	11.5027465634661894	53.399416880296549	\N	f
56101	118	11.4981992877617323	53.4091007726438534	\N	f
56101	62	11.4449837009232578	53.3540961150995514	\N	f
56101	70	11.4490124162065783	53.3579167729150541	\N	f
56101	36	11.3951650201193626	53.3324909210016784	\N	f
56101	69	11.448233457987758	53.3568584179176071	\N	f
56101	105	11.5019754283571718	53.3980405717650157	\N	f
56101	65	11.4462832684729232	53.3547269930257002	\N	f
56101	77	11.4633732243636235	53.3597050985627988	\N	f
56101	95	11.4914300626090924	53.3628423518577719	\N	f
56101	59	11.4408866265614328	53.3532530818861659	\N	f
56101	76	11.4621861605834852	53.3596225833808475	\N	f
56101	75	11.4534518447544844	53.3594795322301891	\N	f
56101	22	11.3747675651632445	53.3081286394922884	\N	f
56101	110	11.5030790456327576	53.4003658980212919	\N	f
56101	27	11.3840211864714593	53.3245972169473177	\N	f
56101	18	11.3683557816227321	53.3043018349476583	\N	f
56101	32	11.389851265918864	53.3305286242931018	\N	f
56101	1	11.3668597050054299	53.2768091919304965	\N	f
56101	53	11.4255568703761003	53.3494385707997978	\N	f
56101	41	11.4061214716610646	53.3434499802466462	\N	f
56101	82	11.4697490588519511	53.3604240796064744	\N	f
56101	79	11.4668388620057442	53.3603601908764276	\N	f
56101	96	11.491843569841464	53.3647046245196037	\N	f
56101	19	11.3723826342608625	53.3038028323010238	\N	f
56101	45	11.4081450493964187	53.3440229299073607	\N	f
56101	97	11.4922609886287379	53.3667406819777312	\N	f
56101	117	11.4998847953949443	53.409178817477347	\N	f
56101	34	11.3921857191365739	53.3314146846103938	\N	f
56101	28	11.3853159111436284	53.326561935094638	\N	f
56101	88	11.4797164457973047	53.3590611821203211	\N	f
56101	119	11.4971598281581588	53.4090213635754907	\N	t
56101	83	11.4706516967114442	53.3601867786091617	\N	f
56101	12	11.366174437839712	53.2890549664733157	\N	f
56101	101	11.4973564408128652	53.3908770246254747	\N	f
56101	20	11.3731329077437433	53.305191248026297	\N	f
56101	49	11.4115488472182047	53.3433557303999919	\N	f
56101	60	11.4420364374377534	53.3534346897922731	\N	f
56101	3	11.3668621264441789	53.2774149241465693	\N	f
56101	113	11.5017889775735664	53.4028037280470969	\N	f
56101	85	11.4710493714596939	53.3600781863945315	\N	f
56101	21	11.3740713083908993	53.3068184548650308	\N	f
56101	78	11.4643689944826583	53.3598485222425012	\N	f
56101	71	11.4497189175274734	53.3585068589115181	\N	f
56101	106	11.5026904978459434	53.39931816010143	\N	f
56101	98	11.492782529282179	53.3692813300180759	\N	f
56101	17	11.3683809273328098	53.3039624609938798	\N	f
56101	112	11.5028480776290909	53.4010761246325671	\N	f
56101	52	11.415943944810568	53.3442123609232652	\N	f
56101	2	11.3668634302958118	53.277110195393341	\N	f
56101	93	11.4892433171550188	53.3578916272049781	\N	f
56101	108	11.5030032359734893	53.3998456612194872	\N	f
56101	84	11.4708519310694612	53.3601357421309288	\N	f
56101	116	11.5001327134698492	53.4080377610334267	\N	f
56101	10	11.3663169301968132	53.2862775762292173	\N	f
56101	103	11.4995448626476122	53.3945687873937658	\N	f
56101	61	11.4441822046976291	53.3538446579987848	\N	f
56101	7	11.3666896454995054	53.2809176284279857	\N	f
56101	100	11.4970470554466626	53.3892911685099705	\N	f
56101	109	11.5030555763033533	53.4000395625838564	\N	f
56101	0	11.3668551553295369	53.2764918887278824	\N	t
56101	68	11.4479296605571292	53.3564456557433076	\N	f
56101	14	11.3658294759503633	53.2957770667026267	\N	f
56101	54	11.4276730215774407	53.3505228165653946	\N	f
56101	111	11.5030134805220392	53.400697635129788	\N	f
56101	57	11.4316508866470503	53.3516415212670339	\N	f
56101	102	11.4979098326990723	53.3925997851625027	\N	f
56101	35	11.3947306512608524	53.3323100581536451	\N	f
56101	66	11.4467701639258159	53.355085924753979	\N	f
56151	33	11.3910042432920093	53.3310145884234004	\N	f
56151	58	11.4369724639573498	53.3526151259082937	\N	f
56151	81	11.4687290743454344	53.3605332306146565	\N	f
56151	80	11.4677370295167798	53.3605015656464161	\N	f
56151	101	11.4973564408128652	53.3908770246254747	\N	f
56151	95	11.4914300626090924	53.3628423518577719	\N	f
56151	67	11.4471706326418516	53.3554709335149298	\N	f
56151	43	11.4068983809701745	53.3438806238147762	\N	f
56151	114	11.5011450611310835	53.4050720573605346	\N	f
56151	24	11.3761535594497669	53.3121620113885868	\N	f
56151	56	11.4291759899819496	53.3511384208009787	\N	f
56151	55	11.4286229706247813	53.3509664986498606	\N	f
56151	65	11.4462832684729232	53.3547269930257002	\N	f
56151	1	11.3668597050054299	53.2768091919304965	\N	f
56151	19	11.3723826342608625	53.3038028323010238	\N	f
56151	44	11.4075396897093881	53.3440726625339536	\N	f
56151	70	11.4490124162065783	53.3579167729150541	\N	f
56151	8	11.3666140221047556	53.2822030398742044	\N	f
56151	83	11.4706516967114442	53.3601867786091617	\N	f
56151	66	11.4467701639258159	53.355085924753979	\N	f
56151	4	11.3668269224500715	53.2781605410164687	\N	f
56151	71	11.4497189175274734	53.3585068589115181	\N	f
56151	76	11.4621861605834852	53.3596225833808475	\N	f
56151	115	11.5009068288111731	53.4059102476964185	\N	f
56151	48	11.4103845077093951	53.3436043935329707	\N	f
56151	113	11.5017889775735664	53.4028037280470969	\N	f
56151	116	11.5001327134698492	53.4080377610334267	\N	f
56151	28	11.3853159111436284	53.326561935094638	\N	f
56151	46	11.4082055853651223	53.344018087029859	\N	f
56151	42	11.406470903898871	53.3436977120570361	\N	f
56151	103	11.4995448626476122	53.3945687873937658	\N	f
56151	23	11.3736119800868334	53.3083005616434065	\N	f
56151	104	11.5005937181545885	53.3957429989220884	\N	f
56151	16	11.3679217852932606	53.3022754632445128	\N	f
56151	92	11.4885565598731461	53.3581477409187173	\N	f
56151	32	11.389851265918864	53.3305286242931018	\N	f
56151	2	11.3668634302958118	53.277110195393341	\N	f
56151	20	11.3731329077437433	53.305191248026297	\N	f
56151	11	11.3661854274463376	53.2888593887282767	\N	f
56151	52	11.415943944810568	53.3442123609232652	\N	f
56151	63	11.4454696650535546	53.3543056626835224	\N	f
56151	45	11.4081450493964187	53.3440229299073607	\N	f
56151	34	11.3921857191365739	53.3314146846103938	\N	f
56151	89	11.4835570339163482	53.3588307729102098	\N	f
56151	51	11.413180338140883	53.3427071573445346	\N	f
56151	110	11.5030790456327576	53.4003658980212919	\N	f
56151	82	11.4697490588519511	53.3604240796064744	\N	f
56151	61	11.4441822046976291	53.3538446579987848	\N	f
56151	112	11.5028480776290909	53.4010761246325671	\N	f
56151	37	11.3976618959977163	53.3350390196227835	\N	f
56151	50	11.4124784934329657	53.3430893721376975	\N	f
56151	72	11.4503570597698623	53.358840831194243	\N	f
56151	119	11.4971598281581588	53.4090213635754907	\N	t
56151	6	11.3667265258742844	53.2802837702695413	\N	f
56151	57	11.4316508866470503	53.3516415212670339	\N	f
56151	62	11.4449837009232578	53.3540961150995514	\N	f
56151	74	11.4523132097493079	53.3593722438671918	\N	f
56151	25	11.3816856156666351	53.3209203553405473	\N	f
56151	91	11.4874980186111788	53.3584722137109679	\N	f
56151	30	11.386493289168774	53.3283707498394861	\N	f
56151	15	11.3662007011369024	53.2969300440757721	\N	f
56151	49	11.4115488472182047	53.3433557303999919	\N	f
56151	85	11.4710493714596939	53.3600781863945315	\N	f
56151	69	11.448233457987758	53.3568584179176071	\N	f
56151	5	11.3667831502880858	53.2790801289472	\N	f
56151	97	11.4922609886287379	53.3667406819777312	\N	f
56151	17	11.3683809273328098	53.3039624609938798	\N	f
56151	111	11.5030134805220392	53.400697635129788	\N	f
56151	102	11.4979098326990723	53.3925997851625027	\N	f
56151	90	11.486310768566522	53.3586301660231541	\N	f
56151	9	11.3663862205979136	53.2847749803537525	\N	f
56151	106	11.5026904978459434	53.39931816010143	\N	f
56151	41	11.4061214716610646	53.3434499802466462	\N	f
56151	14	11.3658294759503633	53.2957770667026267	\N	f
56151	53	11.4255568703761003	53.3494385707997978	\N	f
56151	21	11.3740713083908993	53.3068184548650308	\N	f
56151	78	11.4643689944826583	53.3598485222425012	\N	f
56151	68	11.4479296605571292	53.3564456557433076	\N	f
56151	39	11.404349723555514	53.3416935058316639	\N	f
56151	40	11.4053866581332688	53.3427775653327458	\N	f
56151	31	11.3876367670514451	53.3291055633672855	\N	f
56151	94	11.4903014858879473	53.3576314156718112	\N	f
56151	86	11.4731929035453408	53.3594795322301891	\N	f
56151	79	11.4668388620057442	53.3603601908764276	\N	f
56151	3	11.3668621264441789	53.2774149241465693	\N	f
56151	60	11.4420364374377534	53.3534346897922731	\N	f
56151	77	11.4633732243636235	53.3597050985627988	\N	f
56151	87	11.4745487229797707	53.3591105422178771	\N	f
56151	0	11.3668551553295369	53.2764918887278824	\N	t
56151	10	11.3663169301968132	53.2862775762292173	\N	f
56151	107	11.5027465634661894	53.399416880296549	\N	f
56151	54	11.4276730215774407	53.3505228165653946	\N	f
56151	98	11.492782529282179	53.3692813300180759	\N	f
56151	12	11.366174437839712	53.2890549664733157	\N	f
56151	13	11.3658399067634317	53.2955755284929751	\N	f
56151	109	11.5030555763033533	53.4000395625838564	\N	f
56151	64	11.4457503656838178	53.3544299011177543	\N	f
56151	88	11.4797164457973047	53.3590611821203211	\N	f
56151	47	11.4092112275036701	53.3438225092848199	\N	f
56151	96	11.491843569841464	53.3647046245196037	\N	f
56151	22	11.3747675651632445	53.3081286394922884	\N	f
56151	99	11.494642194240738	53.378353715949217	\N	f
56151	26	11.3827156584571814	53.3225242791144041	\N	f
56151	105	11.5019754283571718	53.3980405717650157	\N	f
56151	27	11.3840211864714593	53.3245972169473177	\N	f
56151	73	11.451422865348075	53.3591833716448392	\N	f
56151	84	11.4708519310694612	53.3601357421309288	\N	f
56151	93	11.4892433171550188	53.3578916272049781	\N	f
56151	35	11.3947306512608524	53.3323100581536451	\N	f
56151	100	11.4970470554466626	53.3892911685099705	\N	f
56151	59	11.4408866265614328	53.3532530818861659	\N	f
56151	75	11.4534518447544844	53.3594795322301891	\N	f
56151	29	11.3860019233674237	53.3276478572409118	\N	f
56151	7	11.3666896454995054	53.2809176284279857	\N	f
56151	118	11.4981992877617323	53.4091007726438534	\N	f
56151	108	11.5030032359734893	53.3998456612194872	\N	f
56151	117	11.4998847953949443	53.409178817477347	\N	f
56151	38	11.4021251664040655	53.3394978196806733	\N	f
56151	36	11.3951650201193626	53.3324909210016784	\N	f
56151	18	11.3683557816227321	53.3043018349476583	\N	f
56251	94	11.4903014858879473	53.3576314156718112	\N	f
56251	82	11.4697490588519511	53.3604240796064744	\N	f
56251	96	11.491843569841464	53.3647046245196037	\N	f
56251	22	11.3747675651632445	53.3081286394922884	\N	f
56251	56	11.4291759899819496	53.3511384208009787	\N	f
56251	102	11.4979098326990723	53.3925997851625027	\N	f
56251	39	11.404349723555514	53.3416935058316639	\N	f
56251	28	11.3853159111436284	53.326561935094638	\N	f
56251	78	11.4643689944826583	53.3598485222425012	\N	f
56251	34	11.3921857191365739	53.3314146846103938	\N	f
56251	106	11.5026904978459434	53.39931816010143	\N	f
56251	74	11.4523132097493079	53.3593722438671918	\N	f
56251	21	11.3740713083908993	53.3068184548650308	\N	f
56251	61	11.4441822046976291	53.3538446579987848	\N	f
56251	10	11.3663169301968132	53.2862775762292173	\N	f
56251	18	11.3683557816227321	53.3043018349476583	\N	f
56251	79	11.4668388620057442	53.3603601908764276	\N	f
56251	73	11.451422865348075	53.3591833716448392	\N	f
56251	15	11.3662007011369024	53.2969300440757721	\N	f
56251	50	11.4124784934329657	53.3430893721376975	\N	f
56251	40	11.4053866581332688	53.3427775653327458	\N	f
56251	97	11.4922609886287379	53.3667406819777312	\N	f
56251	23	11.3736119800868334	53.3083005616434065	\N	f
56251	68	11.4479296605571292	53.3564456557433076	\N	f
56251	110	11.5030790456327576	53.4003658980212919	\N	f
56251	19	11.3723826342608625	53.3038028323010238	\N	f
56251	114	11.5011450611310835	53.4050720573605346	\N	f
56251	81	11.4687290743454344	53.3605332306146565	\N	f
56251	1	11.3668597050054299	53.2768091919304965	\N	f
56251	14	11.3658294759503633	53.2957770667026267	\N	f
56251	33	11.3910042432920093	53.3310145884234004	\N	f
56251	67	11.4471706326418516	53.3554709335149298	\N	f
56251	63	11.4454696650535546	53.3543056626835224	\N	f
56251	44	11.4075396897093881	53.3440726625339536	\N	f
56251	116	11.5001327134698492	53.4080377610334267	\N	f
56251	108	11.5030032359734893	53.3998456612194872	\N	f
56251	72	11.4503570597698623	53.358840831194243	\N	f
56251	85	11.4710493714596939	53.3600781863945315	\N	f
56251	17	11.3683809273328098	53.3039624609938798	\N	f
56251	55	11.4286229706247813	53.3509664986498606	\N	f
56251	42	11.406470903898871	53.3436977120570361	\N	f
56251	100	11.4970470554466626	53.3892911685099705	\N	f
56251	3	11.3668621264441789	53.2774149241465693	\N	f
56251	105	11.5019754283571718	53.3980405717650157	\N	f
56251	113	11.5017889775735664	53.4028037280470969	\N	f
56251	49	11.4115488472182047	53.3433557303999919	\N	f
56251	25	11.3816856156666351	53.3209203553405473	\N	f
56251	6	11.3667265258742844	53.2802837702695413	\N	f
56251	59	11.4408866265614328	53.3532530818861659	\N	f
56251	80	11.4677370295167798	53.3605015656464161	\N	f
56251	66	11.4467701639258159	53.355085924753979	\N	f
56251	70	11.4490124162065783	53.3579167729150541	\N	f
56251	35	11.3947306512608524	53.3323100581536451	\N	f
56251	0	11.3668551553295369	53.2764918887278824	\N	t
56251	38	11.4021251664040655	53.3394978196806733	\N	f
56251	45	11.4081450493964187	53.3440229299073607	\N	f
56251	99	11.494642194240738	53.378353715949217	\N	f
56251	47	11.4092112275036701	53.3438225092848199	\N	f
56251	84	11.4708519310694612	53.3601357421309288	\N	f
56251	109	11.5030555763033533	53.4000395625838564	\N	f
56251	53	11.4255568703761003	53.3494385707997978	\N	f
56251	9	11.3663862205979136	53.2847749803537525	\N	f
56251	4	11.3668269224500715	53.2781605410164687	\N	f
56251	5	11.3667831502880858	53.2790801289472	\N	f
56251	62	11.4449837009232578	53.3540961150995514	\N	f
56251	91	11.4874980186111788	53.3584722137109679	\N	f
56251	104	11.5005937181545885	53.3957429989220884	\N	f
56251	8	11.3666140221047556	53.2822030398742044	\N	f
56251	2	11.3668634302958118	53.277110195393341	\N	f
56251	26	11.3827156584571814	53.3225242791144041	\N	f
56251	90	11.486310768566522	53.3586301660231541	\N	f
56251	112	11.5028480776290909	53.4010761246325671	\N	f
56251	57	11.4316508866470503	53.3516415212670339	\N	f
56251	58	11.4369724639573498	53.3526151259082937	\N	f
56251	11	11.3661854274463376	53.2888593887282767	\N	f
56251	48	11.4103845077093951	53.3436043935329707	\N	f
56251	87	11.4745487229797707	53.3591105422178771	\N	f
56251	103	11.4995448626476122	53.3945687873937658	\N	f
56251	60	11.4420364374377534	53.3534346897922731	\N	f
56251	20	11.3731329077437433	53.305191248026297	\N	f
56251	41	11.4061214716610646	53.3434499802466462	\N	f
56251	52	11.415943944810568	53.3442123609232652	\N	f
56251	27	11.3840211864714593	53.3245972169473177	\N	f
56251	95	11.4914300626090924	53.3628423518577719	\N	f
56251	117	11.4998847953949443	53.409178817477347	\N	f
56251	89	11.4835570339163482	53.3588307729102098	\N	f
56251	51	11.413180338140883	53.3427071573445346	\N	f
56251	32	11.389851265918864	53.3305286242931018	\N	f
56251	101	11.4973564408128652	53.3908770246254747	\N	f
56251	12	11.366174437839712	53.2890549664733157	\N	f
56251	16	11.3679217852932606	53.3022754632445128	\N	f
56251	92	11.4885565598731461	53.3581477409187173	\N	f
56251	71	11.4497189175274734	53.3585068589115181	\N	f
56251	83	11.4706516967114442	53.3601867786091617	\N	f
56251	77	11.4633732243636235	53.3597050985627988	\N	f
56251	118	11.4981992877617323	53.4091007726438534	\N	f
56251	36	11.3951650201193626	53.3324909210016784	\N	f
56251	24	11.3761535594497669	53.3121620113885868	\N	f
56251	30	11.386493289168774	53.3283707498394861	\N	f
56251	37	11.3976618959977163	53.3350390196227835	\N	f
56251	86	11.4731929035453408	53.3594795322301891	\N	f
56251	69	11.448233457987758	53.3568584179176071	\N	f
56251	76	11.4621861605834852	53.3596225833808475	\N	f
56251	29	11.3860019233674237	53.3276478572409118	\N	f
56251	46	11.4082055853651223	53.344018087029859	\N	f
56251	107	11.5027465634661894	53.399416880296549	\N	f
56251	119	11.4971598281581588	53.4090213635754907	\N	t
56251	54	11.4276730215774407	53.3505228165653946	\N	f
56251	115	11.5009068288111731	53.4059102476964185	\N	f
56251	31	11.3876367670514451	53.3291055633672855	\N	f
56251	88	11.4797164457973047	53.3590611821203211	\N	f
56251	111	11.5030134805220392	53.400697635129788	\N	f
56251	7	11.3666896454995054	53.2809176284279857	\N	f
56251	93	11.4892433171550188	53.3578916272049781	\N	f
56251	13	11.3658399067634317	53.2955755284929751	\N	f
56251	43	11.4068983809701745	53.3438806238147762	\N	f
56251	65	11.4462832684729232	53.3547269930257002	\N	f
56251	75	11.4534518447544844	53.3594795322301891	\N	f
56251	98	11.492782529282179	53.3692813300180759	\N	f
56251	64	11.4457503656838178	53.3544299011177543	\N	f
56252	14	11.3658294759503633	53.2957770667026267	\N	f
56252	12	11.366174437839712	53.2890549664733157	\N	f
56252	46	11.4082055853651223	53.344018087029859	\N	f
56252	110	11.5030790456327576	53.4003658980212919	\N	f
56252	15	11.3662007011369024	53.2969300440757721	\N	f
56252	61	11.4441822046976291	53.3538446579987848	\N	f
56252	57	11.4316508866470503	53.3516415212670339	\N	f
56252	18	11.3683557816227321	53.3043018349476583	\N	f
56252	47	11.4092112275036701	53.3438225092848199	\N	f
56252	83	11.4706516967114442	53.3601867786091617	\N	f
56252	4	11.3668269224500715	53.2781605410164687	\N	f
56252	35	11.3947306512608524	53.3323100581536451	\N	f
56252	41	11.4061214716610646	53.3434499802466462	\N	f
56252	96	11.491843569841464	53.3647046245196037	\N	f
56252	25	11.3816856156666351	53.3209203553405473	\N	f
56252	33	11.3910042432920093	53.3310145884234004	\N	f
56252	59	11.4408866265614328	53.3532530818861659	\N	f
56252	39	11.404349723555514	53.3416935058316639	\N	f
56252	53	11.4255568703761003	53.3494385707997978	\N	f
56252	67	11.4471706326418516	53.3554709335149298	\N	f
56252	23	11.3736119800868334	53.3083005616434065	\N	f
56252	80	11.4677370295167798	53.3605015656464161	\N	f
56252	119	11.4971598281581588	53.4090213635754907	\N	t
56252	68	11.4479296605571292	53.3564456557433076	\N	f
56252	98	11.492782529282179	53.3692813300180759	\N	f
56252	63	11.4454696650535546	53.3543056626835224	\N	f
56252	78	11.4643689944826583	53.3598485222425012	\N	f
56252	28	11.3853159111436284	53.326561935094638	\N	f
56252	43	11.4068983809701745	53.3438806238147762	\N	f
56252	112	11.5028480776290909	53.4010761246325671	\N	f
56252	27	11.3840211864714593	53.3245972169473177	\N	f
56252	3	11.3668621264441789	53.2774149241465693	\N	f
56252	85	11.4710493714596939	53.3600781863945315	\N	f
56252	115	11.5009068288111731	53.4059102476964185	\N	f
56252	64	11.4457503656838178	53.3544299011177543	\N	f
56252	103	11.4995448626476122	53.3945687873937658	\N	f
56252	31	11.3876367670514451	53.3291055633672855	\N	f
56252	56	11.4291759899819496	53.3511384208009787	\N	f
56252	72	11.4503570597698623	53.358840831194243	\N	f
56252	107	11.5027465634661894	53.399416880296549	\N	f
56252	10	11.3663169301968132	53.2862775762292173	\N	f
56252	82	11.4697490588519511	53.3604240796064744	\N	f
56252	84	11.4708519310694612	53.3601357421309288	\N	f
56252	7	11.3666896454995054	53.2809176284279857	\N	f
56252	50	11.4124784934329657	53.3430893721376975	\N	f
56252	100	11.4970470554466626	53.3892911685099705	\N	f
56252	81	11.4687290743454344	53.3605332306146565	\N	f
56252	36	11.3951650201193626	53.3324909210016784	\N	f
56252	106	11.5026904978459434	53.39931816010143	\N	f
56252	116	11.5001327134698492	53.4080377610334267	\N	f
56252	92	11.4885565598731461	53.3581477409187173	\N	f
56252	113	11.5017889775735664	53.4028037280470969	\N	f
56252	65	11.4462832684729232	53.3547269930257002	\N	f
56252	16	11.3679217852932606	53.3022754632445128	\N	f
56252	77	11.4633732243636235	53.3597050985627988	\N	f
56252	89	11.4835570339163482	53.3588307729102098	\N	f
56252	40	11.4053866581332688	53.3427775653327458	\N	f
56252	91	11.4874980186111788	53.3584722137109679	\N	f
56252	109	11.5030555763033533	53.4000395625838564	\N	f
56252	101	11.4973564408128652	53.3908770246254747	\N	f
56252	99	11.494642194240738	53.378353715949217	\N	f
56252	54	11.4276730215774407	53.3505228165653946	\N	f
56252	52	11.415943944810568	53.3442123609232652	\N	f
56252	42	11.406470903898871	53.3436977120570361	\N	f
56252	5	11.3667831502880858	53.2790801289472	\N	f
56252	0	11.3668551553295369	53.2764918887278824	\N	t
56252	117	11.4998847953949443	53.409178817477347	\N	f
56252	8	11.3666140221047556	53.2822030398742044	\N	f
56252	32	11.389851265918864	53.3305286242931018	\N	f
56252	26	11.3827156584571814	53.3225242791144041	\N	f
56252	69	11.448233457987758	53.3568584179176071	\N	f
56252	9	11.3663862205979136	53.2847749803537525	\N	f
56252	6	11.3667265258742844	53.2802837702695413	\N	f
56252	19	11.3723826342608625	53.3038028323010238	\N	f
56252	94	11.4903014858879473	53.3576314156718112	\N	f
56252	51	11.413180338140883	53.3427071573445346	\N	f
56252	88	11.4797164457973047	53.3590611821203211	\N	f
56252	114	11.5011450611310835	53.4050720573605346	\N	f
56252	93	11.4892433171550188	53.3578916272049781	\N	f
56252	22	11.3747675651632445	53.3081286394922884	\N	f
56252	86	11.4731929035453408	53.3594795322301891	\N	f
56252	55	11.4286229706247813	53.3509664986498606	\N	f
56252	108	11.5030032359734893	53.3998456612194872	\N	f
56252	95	11.4914300626090924	53.3628423518577719	\N	f
56252	60	11.4420364374377534	53.3534346897922731	\N	f
56252	21	11.3740713083908993	53.3068184548650308	\N	f
56252	105	11.5019754283571718	53.3980405717650157	\N	f
56252	79	11.4668388620057442	53.3603601908764276	\N	f
56252	44	11.4075396897093881	53.3440726625339536	\N	f
56252	90	11.486310768566522	53.3586301660231541	\N	f
56252	24	11.3761535594497669	53.3121620113885868	\N	f
56252	66	11.4467701639258159	53.355085924753979	\N	f
56252	111	11.5030134805220392	53.400697635129788	\N	f
56252	118	11.4981992877617323	53.4091007726438534	\N	f
56252	37	11.3976618959977163	53.3350390196227835	\N	f
56252	74	11.4523132097493079	53.3593722438671918	\N	f
56252	34	11.3921857191365739	53.3314146846103938	\N	f
56252	76	11.4621861605834852	53.3596225833808475	\N	f
56252	62	11.4449837009232578	53.3540961150995514	\N	f
56252	29	11.3860019233674237	53.3276478572409118	\N	f
56252	17	11.3683809273328098	53.3039624609938798	\N	f
56252	75	11.4534518447544844	53.3594795322301891	\N	f
56252	13	11.3658399067634317	53.2955755284929751	\N	f
56252	20	11.3731329077437433	53.305191248026297	\N	f
56252	87	11.4745487229797707	53.3591105422178771	\N	f
56252	104	11.5005937181545885	53.3957429989220884	\N	f
56252	73	11.451422865348075	53.3591833716448392	\N	f
56252	2	11.3668634302958118	53.277110195393341	\N	f
56252	30	11.386493289168774	53.3283707498394861	\N	f
56252	49	11.4115488472182047	53.3433557303999919	\N	f
56252	1	11.3668597050054299	53.2768091919304965	\N	f
56252	48	11.4103845077093951	53.3436043935329707	\N	f
56252	45	11.4081450493964187	53.3440229299073607	\N	f
56252	38	11.4021251664040655	53.3394978196806733	\N	f
56252	58	11.4369724639573498	53.3526151259082937	\N	f
56252	102	11.4979098326990723	53.3925997851625027	\N	f
56252	71	11.4497189175274734	53.3585068589115181	\N	f
56252	70	11.4490124162065783	53.3579167729150541	\N	f
56252	97	11.4922609886287379	53.3667406819777312	\N	f
56252	11	11.3661854274463376	53.2888593887282767	\N	f
\.


--
-- Data for Name: sequence; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY sequence (seq_name, seq_count) FROM stdin;
SEQ_GEN\n	1000
SEQ_GEN	56350
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: waypoint; Type: TABLE DATA; Schema: public; Owner: openride
--

COPY waypoint (waypoint_id, ride_id, route_idx, longitude, latitude, description) FROM stdin;
55903	55902	0	11.4884287485519003	53.3249254500000021	Schloss Ludwigslust
55906	55902	1	11.5871648999999994	53.3825266999999997	Neustadt-Glewe
\.


--
-- Name: RegsitrationPass_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY registration_pass
    ADD CONSTRAINT "RegsitrationPass_pkey" PRIMARY KEY (id);


--
-- Name: accounthistory_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY accounthistory
    ADD CONSTRAINT accounthistory_pkey PRIMARY KEY (cust_id, account_timestamp);


--
-- Name: cardetails_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY cardetails
    ADD CONSTRAINT cardetails_pkey PRIMARY KEY (cardet_id);


--
-- Name: customer_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (cust_id);


--
-- Name: drive_route_point_pk; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY drive_route_point
    ADD CONSTRAINT drive_route_point_pk PRIMARY KEY (drive_id, route_idx);


--
-- Name: driverundertakesride_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY driverundertakesride
    ADD CONSTRAINT driverundertakesride_pkey PRIMARY KEY (ride_id);


--
-- Name: favoritepoints_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY favoritepoint
    ADD CONSTRAINT favoritepoints_pkey PRIMARY KEY (favpt_id);


--
-- Name: match_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY match
    ADD CONSTRAINT match_pkey PRIMARY KEY (riderroute_id, ride_id);


--
-- Name: nickname_unique; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT nickname_unique UNIQUE (cust_nickname);


--
-- Name: registration_pass_passcode_key; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY registration_pass
    ADD CONSTRAINT registration_pass_passcode_key UNIQUE (passcode);


--
-- Name: riderundertakesride_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY riderundertakesride
    ADD CONSTRAINT riderundertakesride_pkey PRIMARY KEY (riderroute_id);


--
-- Name: route_point_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY route_point
    ADD CONSTRAINT route_point_pkey PRIMARY KEY (ride_id, route_idx);


--
-- Name: sequence_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY sequence
    ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);


--
-- Name: uc_favoritepoints_cust_id_displayname; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY favoritepoint
    ADD CONSTRAINT uc_favoritepoints_cust_id_displayname UNIQUE (cust_id, favpt_displayname);


--
-- Name: waypoint_pkey; Type: CONSTRAINT; Schema: public; Owner: openride; Tablespace: 
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT waypoint_pkey PRIMARY KEY (waypoint_id);


--
-- Name: drive_route_point_trigger; Type: TRIGGER; Schema: public; Owner: openride
--

CREATE TRIGGER drive_route_point_trigger BEFORE INSERT OR UPDATE ON drive_route_point FOR EACH ROW EXECUTE PROCEDURE adjustgeomdriveroutepoint();


--
-- Name: riderundertakesride_trigger; Type: TRIGGER; Schema: public; Owner: openride
--

CREATE TRIGGER riderundertakesride_trigger BEFORE INSERT OR UPDATE ON riderundertakesride FOR EACH ROW EXECUTE PROCEDURE adjustgeomriderundertakesride();


--
-- Name: drive_route_point_fk; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY drive_route_point
    ADD CONSTRAINT drive_route_point_fk FOREIGN KEY (drive_id) REFERENCES driverundertakesride(ride_id) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: fk_accounthistory_cust_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY accounthistory
    ADD CONSTRAINT fk_accounthistory_cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id);


--
-- Name: fk_cardetails_cust_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY cardetails
    ADD CONSTRAINT fk_cardetails_cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id) ON DELETE RESTRICT;


--
-- Name: fk_driverundertakesride_cust_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY driverundertakesride
    ADD CONSTRAINT fk_driverundertakesride_cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id);


--
-- Name: fk_favoritepoints_cust_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY favoritepoint
    ADD CONSTRAINT fk_favoritepoints_cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id);


--
-- Name: fk_riderundertakesride_cust_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY riderundertakesride
    ADD CONSTRAINT fk_riderundertakesride_cust_id FOREIGN KEY (cust_id, account_timestamp) REFERENCES accounthistory(cust_id, account_timestamp);


--
-- Name: fk_riderundertakesride_ride_id; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY riderundertakesride
    ADD CONSTRAINT fk_riderundertakesride_ride_id FOREIGN KEY (ride_id) REFERENCES driverundertakesride(ride_id);


--
-- Name: registration_pass_custid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY registration_pass
    ADD CONSTRAINT registration_pass_custid_fkey FOREIGN KEY (cust_id) REFERENCES customer(cust_id);


--
-- Name: rideid; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY match
    ADD CONSTRAINT rideid FOREIGN KEY (ride_id) REFERENCES driverundertakesride(ride_id);


--
-- Name: riderrouteid; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY match
    ADD CONSTRAINT riderrouteid FOREIGN KEY (riderroute_id) REFERENCES riderundertakesride(riderroute_id);


--
-- Name: route_point_ride_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY route_point
    ADD CONSTRAINT route_point_ride_id_fkey FOREIGN KEY (ride_id) REFERENCES driverundertakesride(ride_id);


--
-- Name: waypoint_ride_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: openride
--

ALTER TABLE ONLY waypoint
    ADD CONSTRAINT waypoint_ride_id_fkey FOREIGN KEY (ride_id) REFERENCES driverundertakesride(ride_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO openride;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

