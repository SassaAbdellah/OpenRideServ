--
-- add an auxiliary column is_countermanded,  that 
-- will be set to true if driverundertakesride is countermanded 
--
--
ALTER TABLE driverundertakesride add COLUMN is_countermanded boolean;
