--
-- add an auxiliary column "last matching state" that describes the
-- last known status of negotiations with potential riders
--
--
ALTER TABLE driverundertakesride add COLUMN last_matching_state int;
