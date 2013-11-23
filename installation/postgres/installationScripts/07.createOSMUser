#!/bin/sh
#
#
# create user for OSM Rendering
#
#
#
#
# drop potentially existing OSM user, 
# to make this idempotent
#
dropuser osm 
#
# create user osm, 
# with grants to create databases, new users, log in to db, create new roles
#
#
#
createuser                  \
             --createdb     \
             --createrole   \
             --login        \
             --superuser    \
             --unencrypted  \
             -P             \
             osm
