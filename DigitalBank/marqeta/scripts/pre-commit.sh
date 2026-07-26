#!/bin/sh
echo '[Git Hook] Executing Spotless check before commit'
cd marqeta
mvn spotless:apply
cd ../core
mvn spotless:apply
exit $?
