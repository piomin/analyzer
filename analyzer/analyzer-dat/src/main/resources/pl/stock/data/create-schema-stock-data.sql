-- drop tables --
DROP TABLE IF EXISTS company CASCADE;
DROP TABLE IF EXISTS update_history CASCADE;
DROP TABLE IF EXISTS daily_quote_record CASCADE;
DROP TABLE IF EXISTS statistic_record CASCADE;


-- create tables --
CREATE TABLE update_history ( 
	id integer NOT NULL,
	status smallint NOT NULL,
	add_date timestamp NOT NULL
);

CREATE TABLE company ( 
	id integer NOT NULL,
	symbol varchar(10) NOT NULL,
	name varchar(50) NOT NULL
);

CREATE TABLE daily_quote_record ( 
	id integer NOT NULL,
	max_prize real NOT NULL,
	min_prize real NOT NULL,
	close_prize real NOT NULL,
	open_prize real NOT NULL,
	volumen real NOT NULL,
	add_date timestamp NOT NULL,
	company_id integer NOT NULL
);

CREATE TABLE statistic_record ( 
	id integer NOT NULL,
	rsi real NOT NULL,
	ema5 real NOT NULL,
	ema10 real NOT NULL,
	ema20 real NOT NULL,
	ema50 real NOT NULL,
	ema100 real NOT NULL,
	ema12 real NOT NULL,
	ema14 real NOT NULL,
	ema26 real NOT NULL,
	average_vol5 real NOT NULL,
	average_vol12 real NOT NULL,
	average_vol26 real NOT NULL,
	average_vol50 real NOT NULL,
	roc real NOT NULL,
	sroc real NOT NULL,
	sts real NOT NULL,
	sts_ema real NOT NULL,
	dmi_plus real NOT NULL,
	dmi_minus real NOT NULL,
	adx real NOT NULL,
	macd real NOT NULL,
	macd_ema real NOT NULL,
	atr real NOT NULL,
	sma14 real NOT NULL,
	sma28 real NOT NULL,
	sma42 real NOT NULL,
	add_date timestamp NOT NULL,
	quote_id integer NOT NULL
);


-- add primery keys --
ALTER TABLE update_history ADD CONSTRAINT pk_update_history PRIMARY KEY (id);
ALTER TABLE company ADD CONSTRAINT pk_company PRIMARY KEY (id);
ALTER TABLE daily_quote_record ADD CONSTRAINT pk_daily_quote_record PRIMARY KEY (id);
ALTER TABLE statistic_record ADD CONSTRAINT pk_statistic_record PRIMARY KEY (id);


-- add unique keys --
ALTER TABLE company ADD CONSTRAINT uq_company_symbol UNIQUE (symbol);
ALTER TABLE company ADD CONSTRAINT uq_company_name UNIQUE (name);


-- add foreign keys --
ALTER TABLE daily_quote_record ADD CONSTRAINT fk_daily_quote_record FOREIGN KEY (company_id) REFERENCES company (id);
ALTER TABLE statistic_record ADD CONSTRAINT fk_statistic_record FOREIGN KEY (quote_id) REFERENCES daily_quote_record (id);


-- drop sequences --
DROP SEQUENCE IF EXISTS company_seq;
DROP SEQUENCE IF EXISTS update_history_seq;
DROP SEQUENCE IF EXISTS daily_quote_record_seq;
DROP SEQUENCE IF EXISTS statistic_record_seq;


-- add sequences --
CREATE SEQUENCE company_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE update_history_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE daily_quote_record_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE statistic_record_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;


-- grants --
ALTER TABLE company_seq OWNER TO stockgpw;
ALTER TABLE update_history_seq OWNER TO stockgpw;
ALTER TABLE daily_quote_record_seq OWNER TO stockgpw;
ALTER TABLE statistic_record_seq OWNER TO stockgpw;