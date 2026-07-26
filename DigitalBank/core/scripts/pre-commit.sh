#!/bin/sh
echo '[Git Hook] Executing Spotless check before commit'
cd core
mvn spotless:apply
cd ../marqeta
mvn spotless:apply
exit $?
