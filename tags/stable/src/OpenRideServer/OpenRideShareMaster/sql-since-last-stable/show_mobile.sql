--
-- Alter Customer Table to contain a "show_mobile" column.
-- show mobile='t' means, that customer wants his cellphone number
-- to be shown in public profile
--
ALTER TABLE customer ADD COLUMN show_mobile boolean;

