#!/bin/sh
#
#
# Source glassfish configuration
#
. ./00.glassfish.conf
#
#
# REAM_NAME and JNDI_NAME are read from 00.glassfish.conf
#
#
asadmin  delete-auth-realm    ${REALM_NAME}
asadmin  delete-jdbc-resource ${JNDI_NAME}
asadmin  delete-jdbc-resource ${JNDI_NAME_RM}
#
#
#
#
