#!/bin/bash
date

LIBRETIME_VERSION=4.0.0
# Load LIBRETIME_VERSION variable
echo "======= TEST ======="

echo "====================================================================="
# Create LibreTime admin password file psql.setadminpsw.template.sql
echo "Prepare config LIBRETIME files"
bash -a -c "envsubst < $CLIENT_DIR/template/psql.setadminpsw.template.sql > $CLIENT_DIR/tmp/$STATION_UUID.sql; scp  $CLIENT_DIR/tmp/$STATION_UUID.sql root@$LIBRETIME_POSTGRESQL_HOST:/root/"

cat $CLIENT_DIR/tmp/$STATION_UUID.sql
PGPASSWORD=$LIBRETIME_POSTGRESQL_DBNAME psql -h pg.rfa -U $LIBRETIME_POSTGRESQL_DBNAME -d $LIBRETIME_POSTGRESQL_DBNAME < $CLIENT_DIR/tmp/$STATION_UUID.sql 

echo "====================================================================="
echo "SET NEW LIBRETIME admin password"

date
