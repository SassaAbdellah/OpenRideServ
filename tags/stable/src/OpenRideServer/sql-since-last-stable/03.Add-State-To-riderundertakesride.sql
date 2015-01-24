--
-- add an auxiliary column "last matching state" that describes the
-- last known status of negotiations with potential drivers 
--
--
ALTER TABLE riderundertakesride add COLUMN last_matching_state int;
