#!/bin/sh
#
#
# source glassfish configuration
#
#
. ./00.glassfish.conf
#
#
#
# copy jdbc archive to glassfish lib dir 
#
cp -v ${POSTGRES_JDBC_ARCHIVE} "${GLASSFISH_HOME}/glassfish/lib/" 
#
#
#
#
# start glassfish
#
asadmin start-domain domain1
#
#
#
# try to delete the connection pools to be nilpotent
#
# 
#  connection pool named "openride" is here to serve OpenRide's JPA functions 
# 
asadmin delete-jdbc-connection-pool openride
#
#
# create "openride" connection
#
#
asadmin create-jdbc-connection-pool                                                                       \
 --datasourceclassname  org.postgresql.ds.PGSimpleDataSource                                              \
 --property portNumber=5432:password=openride:user=openride:serverName=localhost:databaseName=openride   \
 openride

#
# 








