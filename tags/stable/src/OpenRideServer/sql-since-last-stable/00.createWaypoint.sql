 
drop table waypoint ;

create table waypoint(
 	waypoint_id        integer            not null,
 	ride_id            integer            not null,
 	route_idx          integer            not null,
 	longitude          double precision   not null,
 	latitude           double precision   not null,
 	description        text
);

ALTER TABLE ONLY waypoint ADD CONSTRAINT waypoint_pkey PRIMARY KEY (waypoint_id);

ALTER Table waypoint ADD CONSTRAINT "waypoint_ride_id_fkey" FOREIGN KEY (ride_id) REFERENCES driverundertakesride(ride_id);

