#!/usr/bin/env bash

mvn clean -DskipTests=true package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/currencyrate-1.jar \
    rustem@192.168.0.108:/home/rustem/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa rustem@192.168.0.108 << EOF
pgrep java | xargs kill -9
nohup java -jar currencyrate-1.jar > log.txt &
EOF

echo 'Bye'
