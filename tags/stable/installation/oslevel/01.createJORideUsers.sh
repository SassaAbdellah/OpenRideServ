#!/bin/sh
#
#
#
if [ ! $(id -u ) = 0 ] 
then
	echo 'ERROR :  this script must be run as root' 
	exit 1
fi
#
#
echo creating user postgres with home /opt/postgres
#
useradd --home '/opt/postgres' postgres
groupadd postgres
#
mkdir /opt/postgres
chown -R postgres.postgres /opt/postgres
#
#
#
echo creating user openride with home /opt/openride
#
useradd --home '/opt/openride' openride
groupadd openride
#
mkdir /opt/openride
chown -R openride.openride /opt/openride
#
#


