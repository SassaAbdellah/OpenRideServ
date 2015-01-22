#
# drop all OSM Tables
#
#
echo dropping     hh_distance_table_row
echo 'DROP TABLE  hh_distance_table_row  ;' | psql -d osm  

echo  dropping    hh_edge
echo 'DROP TABLE  hh_edge                ;' | psql -d osm  

echo  dropping    hh_graph_properties
echo 'DROP TABLE  hh_graph_properties    ;' | psql -d osm  

echo  dropping    hh_lvl_staty
echo 'DROP TABLE  hh_lvl_stats           ;' | psql -d osm  

echo dropping     hh_vertex
echo 'DROP TABLE  hh_vertex              ;' | psql -d osm  

echo dropping     hh_vertex_lvl
echo 'DROP TABLE  hh_vertex_lvl          ;' | psql -d osm  

echo dropping    rg_edge
echo 'DROP TABLE rg_edge                ;' | psql -d osm  

echo dropping    rg_hwy_lvl
echo 'DROP TABLE rg_hwy_lvl             ;' | psql -d osm  

echo dropping    rg_vertex
echo 'DROP TABLE rg_vertex              ;' | psql -d osm  
