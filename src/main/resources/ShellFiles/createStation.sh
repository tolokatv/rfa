#!/bin/bash
date
echo "STATION_ID=$STATION_ID"
echo "LIBRETIME_RABBITMQ_VHOST=$LIBRETIME_RABBITMQ_VHOST"
echo "LIBRETIME_RABBITMQ_USERNAME=$LIBRETIME_RABBITMQ_USERNAME"
echo "LIBRETIME_RABBITMQ_PASSWORD=$LIBRETIME_RABBITMQ_PASSWORD"
echo "========= LIBRETIME_RABBITMQ_HOST=$LIBRETIME_RABBITMQ_HOST"
echo " "
echo "LIBRETIME_POSTGRESQL_USERNAME=$LIBRETIME_POSTGRESQL_USERNAME"
echo "LIBRETIME_POSTGRESQL_DBNAME=$LIBRETIME_POSTGRESQL_DBNAME"
echo "LIBRETIME_POSTGRESQL_PASSWORD=$LIBRETIME_POSTGRESQL_PASSWORD"
echo "CLIENT_DIR=$CLIENT_DIR"
echo "CLIENT_UUID=$CLIENT_UUID"
echo "STATION_UUID=$STATION_UUID"
echo "LIBRETIME_TIMEZONE=$LIBRETIME_TIMEZONE"
echo "Home=$HOME"

LIBRETIME_VERSION=4.0.0
# Load LIBRETIME_VERSION variable
echo "LIBRETIME_VERSION=4.0.0" > .env
source .env
echo "# Створюємо каталог клієнта та станції"
echo "====================================================================="
mkdir -p $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID
mkdir -p $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/libretime/storage
mkdir -p $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/libretime/assets
mkdir -p $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/libretime/playout

echo "====================================================================="
echo "Prepare config LIBRETIME files"
bash -a -c "envsubst < $CLIENT_DIR/template/config.template.yml > $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/config.yml"
bash -a -c "envsubst < $CLIENT_DIR/template/docker-compose.template.yml > $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/docker-compose.yml"
cp $CLIENT_DIR/template/nginx.conf $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/nginx.conf
#bash -a -c "envsubst < config.template.yml > config.yml"

echo "====================================================================="
# Create Postgre user
echo "Prepare config LIBRETIME files"
bash -a -c "envsubst < $CLIENT_DIR/template/psql.template.sql > $CLIENT_DIR/tmp/$STATION_UUID.sql; scp $CLIENT_DIR/tmp/$STATION_UUID.sql root@$LIBRETIME_POSTGRESQL_HOST:/root/"
ssh root@$LIBRETIME_POSTGRESQL_HOST "chmod 666 /root/$STATION_UUID.sql; chown postgres:postgres /root/$STATION_UUID.sql; cat /root/$STATION_UUID.sql;sudo -u postgres psql < /root/$STATION_UUID.sql # ; rm /root/$STATION_UUID.sql"

echo "====================================================================="
echo "Create RABBITMQ virtual host, user and sets permissions for the user"

# Create RABBITMQ virtual host, user and sets permissions for the user
echo "========= LIBRETIME_RABBITMQ_HOST=$LIBRETIME_RABBITMQ_HOST"
ssh  root@$LIBRETIME_RABBITMQ_HOST "rabbitmqctl add_user $LIBRETIME_RABBITMQ_USERNAME $LIBRETIME_RABBITMQ_PASSWORD"  
ssh  root@$LIBRETIME_RABBITMQ_HOST "rabbitmqctl set_user_tags $LIBRETIME_RABBITMQ_USERNAME administrator" 
ssh  root@$LIBRETIME_RABBITMQ_HOST "rabbitmqctl add_vhost $LIBRETIME_RABBITMQ_VHOST "
ssh  root@$LIBRETIME_RABBITMQ_HOST "rabbitmqctl set_permissions -p $LIBRETIME_RABBITMQ_VHOST $LIBRETIME_RABBITMQ_USERNAME \".*\" \".*\" \".*\" "

date
#docker-compose run --rm api libretime-api migrate

#docker-compose run --rm api libretime-api migrate
#docker-compose up -d

#docker-compose ps
#docker-compose logs -f
