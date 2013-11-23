#!/bin/sh
#
#
#
# create dabase for osm associated for user "osm"
# 
#
#
#
#
echo enable psql for osm database
#
echo create extension postgis | psql -d osm
#
