#!/bin/sh
#
#
#
# create dabase for OSM rendering associated tp user "osm"
# 
#
#
#
#
echo drop database osm | psql -d openride 
#
createdb -O osm osm 'Database for OSM rendering' 

