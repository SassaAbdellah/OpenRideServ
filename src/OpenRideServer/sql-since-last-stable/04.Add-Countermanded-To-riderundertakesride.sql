--
-- add an auxiliary column is_countermanded,  that 
-- will be set to true if riderundertakesride entity is countermanded 
--
--
ALTER TABLE riderundertakesride add COLUMN is_countermanded boolean;
