#!/bin/sh
#
#
cd ${HOME}/postgres-build
#
# untar postgres archive
#
tar -xjvf  ${ARCHIVE_DIR}/postgresql-${POSTGRES_VERSION}.tar.bz2 
#
#
# this is where we build postgres
#
export POSTGRES_BUILD_DIR="${HOME}/postgres-build/postgresql-${POSTGRES_VERSION}"
#
#
#
cd ${POSTGRES_BUILD_DIR}
#
#
./configure --prefix="${POSTGRES_INSTALL_DIR}"
make
make install

echo  installed Postgresql to "${POSTGRES_INSTALL_DIR}"


