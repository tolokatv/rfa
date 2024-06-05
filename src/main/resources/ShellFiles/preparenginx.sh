#!/bin/bash
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "Prepare Nginx site file config"

# cat $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media
echo "$CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media"
echo "/etc/nginx/site-available/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media.conf"
echo "scp $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media root@rfagate.rfa:/etc/nginx/site-available/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media.conf"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
scp $CLIENT_DIR/$CLIENT_UUID/$STATION_UUID/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media root@rfagate.rfa:/etc/nginx/sites-available/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media.conf
ssh root@rfagate.rfa "ln -s /etc/nginx/sites-available/$LIBRETIME_POSTGRESQL_DBNAME.rfa.toloka.media.conf /etc/nginx/sites-enabled/"
ssh root@rfagate.rfa "systemctl reload  nginx"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
echo "++++++++++++++++++ PREPARE NGINX +++++++++++++++++++"
