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
echo enabline psql for osm database
#
createlang plpgsql osm
#
#
#
#
echo loading lwpostgis.sql
#
psql -f ${POSTGIS_BUILD_DIR}/lwpostgis.sql            -d osm   
#
#
echo loading postgis_comments.sql 
#
#
psql -f ${POSTGIS_BUILD_DIR}/postgis_comments.sql     -d osm  
#
#
#echo loading  spatial_ref_sys.sql
#
#
#psql -f ${POSTGISHOME}/spatial_ref_sys.sql


 
