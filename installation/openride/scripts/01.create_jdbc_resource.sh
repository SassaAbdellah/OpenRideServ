#!/bin/sh
#
#
# source glassfish configuration
#
#
. /opt/openride/etc/openride.glassfish.conf
#
#
#
# copy jdbc archive to glassfish lib dir 
#
cp -v ${POSTGRES_JDBC_ARCHIVE} "${GLASSFISH_HOME}/lib" 
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
asadmin create-jdbc-connection-pool                                                                       \
 --datasourceclassname  org.postgresql.ds.PGSimpleDataSource                                              \
 --property portNumber=5432:password=openride:user=openride:serverName=localhost:databaseName=openride   \
 openride

# asadmin stop-domain domain1









