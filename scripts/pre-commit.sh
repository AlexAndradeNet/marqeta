#!/bin/sh
echo '[Git Hook] Executing Spotless check before commit'
cd DigitalBank

cd marqeta
mvn spotless:apply

cd ../core
mvn spotless:apply

cd ../../DigitalBankQA
mvn spotless:apply
sh scripts/gherkin-lint.sh

exit $?
