#!/bin/bash

if [ -z $1 ]
then
    echo "spark master is empty!"
    exit 1
fi

/usr/spark-2.1.0/bin/spark-submit \
    --class com.smack.spark.CassandraConsumer \
    --master $1 \
    --jars /opt/spark/lib/commons-collections-3.2.2.jar,/opt/spark/lib/commons-configuration-1.10.jar,/opt/spark/lib/commons-lang-2.6.jar,/opt/spark/lib/commons-lang3-3.5.jar,/opt/spark/lib/commons-logging-1.2.jar,/opt/spark/lib/guava-19.0.jar,/opt/spark/lib/hadoop-auth-2.8.0.jar,/opt/spark/lib/hadoop-common-2.8.0.jar,/opt/spark/lib/jackson-annotations-2.8.8.jar,/opt/spark/lib/jackson-core-2.8.8.jar,/opt/spark/lib/jackson-databind-2.8.8.1.jar,/opt/spark/lib/jackson-module-scala_2.11-2.8.8.jar,/opt/spark/lib/joda-convert-1.6.jar,/opt/spark/lib/joda-time-2.9.7.jar,/opt/spark/lib/jsr166e-1.1.0.jar,/opt/spark/lib/kafka_2.11-0.10.2.1.jar,/opt/spark/lib/kafka-clients-0.10.2.1.jar,/opt/spark/lib/log4j-1.2.17.jar,/opt/spark/lib/metrics-core-3.0.2.jar,/opt/spark/lib/metrics-core-2.2.0.jar,/opt/spark/lib/netty-all-4.1.11.Final.jar,/opt/spark/lib/slf4j-api-1.7.16.jar,/opt/spark/lib/slf4j-log4j12-1.7.24.jar,/opt/spark/lib/spark-cassandra-connector_2.11-2.0.2.jar,/opt/spark/lib/spark-core_2.11-2.1.1.jar,/opt/spark/lib/spark-network-common_2.11-2.1.1.jar,/opt/spark/lib/spark-sql_2.11-2.1.1.jar,/opt/spark/lib/spark-streaming_2.11-2.1.1.jar,/opt/spark/lib/spark-streaming-kafka-0-10_2.11-2.1.1.jar,/opt/spark/lib/spark-tags_2.11-2.1.1.jar \
    spark-consumer.jar 