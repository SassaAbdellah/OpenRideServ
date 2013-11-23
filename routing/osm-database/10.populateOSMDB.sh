#!/bin/sh
#
#
#
cat ./sql/hhCreateTables.sql       | psql -d osm
cat ./sql/osm2rgCreateTables.sql   | psql -d osm
cat ./sql/osm2rg.mapsforge.sql     | psql -d osm
