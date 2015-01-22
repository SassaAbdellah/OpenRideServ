--
-- Add fields to determine wether or not there were updates since the
-- customer polled for updates for the last time.
-- This is used to determine wether or not there are changes for this 
-- user in a fast way.
--
--
-- "last customer check"  is set to current timestamp whenever the customer 
--  checks for changes in rides and drives.
-- 
--
ALTER TABLE customer add COLUMN last_customer_check  timestamp;
--
-- "last matching change" is set to current timestamp whenever there
-- are changes at a match. If the change is done by rider, then  
--
ALTER TABLE customer add COLUMN last_matching_change  timestamp;
