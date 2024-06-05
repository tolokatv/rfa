#!/bin/bash
cd /home/rfa/prodact
git pull
./gradlew build -x test
/bin/sh -c 'until ping -c1 8.8.8.8; do sleep 5; done;'
/bin/sh -c 'until ping -c4 pg.rfa; do sleep 1; done;'
export QRFA=/rfa
java -jar build/libs/RFA-1.0.0.war
