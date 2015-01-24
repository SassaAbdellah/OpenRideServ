#
# clear all OSM Tables
#
#
echo cleaning hh_distance_table_row
echo 'select count (*) FROM  hh_distance_table_row  ;' | psql -d osm  
echo 'DELETE           FROM  hh_distance_table_row  ;' | psql -d osm  

echo  cleaning hh_edge
echo 'select count (*) FROM  hh_edge                ;' | psql -d osm  
echo 'DELETE FROM            hh_edge                ;' | psql -d osm  

echo  cleaning hh_graph_properties
echo 'select count (*) FROM  hh_graph_properties    ;' | psql -d osm  
echo 'DELETE FROM            hh_graph_properties    ;' | psql -d osm  

echo  cleaning hh_lvl_staty
echo 'select count (*) FROM  hh_lvl_stats           ;' | psql -d osm  
echo 'DELETE FROM            hh_lvl_stats           ;' | psql -d osm  

echo cleaning hh_vertex
echo 'select count (*) FROM  hh_vertex              ;' | psql -d osm  
echo 'DELETE FROM            hh_vertex              ;' | psql -d osm  

echo cleaning hh_vertex_lvl
echo 'select count (*) FROM  hh_vertex_lvl          ;' | psql -d osm  
echo 'DELETE FROM            hh_vertex_lvl          ;' | psql -d osm  

echo cleaning rg_edge
echo 'select count (*) FROM  rg_edge                ;' | psql -d osm  
echo 'DELETE FROM            rg_edge                ;' | psql -d osm  

echo cleaning rg_hwy_lvl
echo 'select count (*) FROM  rg_hwy_lvl             ;' | psql -d osm  
echo 'DELETE FROM            rg_hwy_lvl             ;' | psql -d osm  

echo cleaning rg_vertex
echo 'select count (*) FROM  rg_vertex              ;' | psql -d osm  
echo 'DELETE FROM            rg_vertex              ;' | psql -d osm  
