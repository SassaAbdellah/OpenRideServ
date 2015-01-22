--
-- Alter Customer Table to contain a "show_email" column.
-- show email='t' means, that customer wants his email address
-- to be shown in public profile
--
ALTER TABLE customer ADD COLUMN show_email boolean;

