#
# clear all OSM Tables
#
#
echo cleaning hh_distance_table_row
echo 'TRUNCATE             hh_distance_table_row  CASCADE ;' | psql -d osm  

echo  cleaning hh_edge
echo 'TRUNCATE             hh_edge                CASCADE ;' | psql -d osm  

echo  cleaning hh_graph_properties
echo 'TRUNCATE             hh_graph_properties    CASCADE ;' | psql -d osm  

echo  cleaning hh_lvl_staty
echo 'TRUNCATE             hh_lvl_stats           CASCADE ;' | psql -d osm  

echo cleaning hh_vertex
echo 'TRUNCATE             hh_vertex              CASCADE ;' | psql -d osm  

echo cleaning hh_vertex_lvl
echo 'TRUNCATE             hh_vertex_lvl          CASCADE ;' | psql -d osm  

echo cleaning rg_edge
echo 'TRUNCATE             rg_edge                CASCADE ;' | psql -d osm  

echo cleaning rg_hwy_lvl
echo 'TRUNCATE             rg_hwy_lvl             CASCADE ;' | psql -d osm  

echo cleaning rg_vertex
echo 'TRUNCATE             rg_vertex              CASCADE ;' | psql -d osm  
