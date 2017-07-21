#!/bin/bash

if [ -z $MASTER_IP ]
then
    echo "MASTER IP is empty!"
    exit 1
fi

NET_IP=${MASTER_IP:-7077}
NET_PORT=${MASTER_PORT:-7077}

/usr/spark-2.1.0/bin/spark-class org.apache.spark.deploy.worker.Worker spark://$NET_IP:$NET_PORT