#!/bin/sh
#
#
#
# populate database for openride from dump 
# 
# creating the hh tables is now no more necessary, as 
# mapsforge import_rg target takes care of this 
#
#psql   -d  osm  < ${HOME}/archives/hhCreateTables.sql
#
#
# importing the osm2rg schema is now deprecated, as 
# mapsforge has added a number of new fields to the schema
#
#psql   -d  osm <  ${HOME}/archives/osm2rgCreateTables.sql
#
# the "new" mapsforge routing schema (post mapsforge 0.1)
# see comments inside sql file for where this was obtained
#
#
psql   -d  osm <  ${HOME}/archives/osm2rg.mapsforge.sql 
