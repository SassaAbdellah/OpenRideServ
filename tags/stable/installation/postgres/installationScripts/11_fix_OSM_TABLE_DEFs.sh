#
# change table definitions for osm, since we found that the 
# pristine sources did not work
#
#
echo 'ALTER TABLE    hh_distance_table_row  OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    hh_edge                OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    hh_graph_properties    OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    hh_lvl_stats           OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    hh_vertex              OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    hh_vertex_lvl          OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    rg_edge                OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    rg_hwy_lvl             OWNER TO osm ;' | psql -d osm  
echo 'ALTER TABLE    rg_vertex              OWNER TO osm ;' | psql -d osm  
#
#
# drop the not-null constraint on rg_edge.ref
#
#
echo 'ALTER TABLE rg_edge ALTER COLUMN ref DROP NOT NULL  ' | psql -d osm
echo 'ALTER TABLE rg_edge ALTER COLUMN roundabout DROP NOT NULL  ' | psql -d osm
echo 'ALTER TABLE rg_edge ALTER COLUMN weight        DROP NOT NULL  ' | psql -d osm
