--
-- add an auxiliary column is_countermanded,  that 
-- will be set to true if riderundertakesride entity is countermanded 
--
--
-- message *from* rider
--
ALTER TABLE match add COLUMN rider_message varchar(255);
--
-- message *from* driver
--
ALTER TABLE match add COLUMN driver_message varchar(255);
