#!/bin/sh
#
#
cd ${HOME}/postgis-build
#
# untar postgres archive
#
tar -xzvf  ${ARCHIVE_DIR}/postgis-${POSTGIS_VERSION}.tar.gz 
#
#
# this is where we build postgis
#
export POSTGIS_BUILD_DIR="${HOME}/postgis-build/postgis-${POSTGIS_VERSION}"
#
#
#
cd ${POSTGIS_BUILD_DIR}
#
#
./configure --prefix="${POSTGRES_INSTALL_DIR}"
make
make install

echo  installed Postgis to "${POSTGRES_INSTALL_DIR}"


