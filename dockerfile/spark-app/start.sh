#!/bin/bash

if [ -z $1 ]
then
    echo "spark master is empty!"
    exit 1
fi

/usr/spark-2.1.0/bin/spark-submit \
    --class com.smack.spark.rpc.SparkRpcForActor \
    --master $1 \
    --jars lib/netty-3.10.6.Final.jar,lib/protobuf-java-2.5.0.jar,lib/spark-catalyst_2.11-2.1.1.jar,lib/akka-stream_2.11-2.4.17.jar,lib/akka-protobuf_2.11-2.4.17.jar,lib/akka-remote_2.11-2.4.17.jar,lib/akka-slf4j_2.11-2.4.17.jar,lib/akka-actor_2.11-2.4.17.jar,lib/akka-kernel_2.11-2.4.17.jar, lib/commons-collections-3.2.2.jar,lib/commons-configuration-1.10.jar,lib/commons-lang-2.6.jar,lib/commons-lang3-3.5.jar,lib/commons-logging-1.2.jar,lib/guava-19.0.jar,lib/hadoop-auth-2.8.0.jar,lib/hadoop-common-2.8.0.jar,lib/jackson-annotations-2.8.8.jar,lib/jackson-core-2.8.8.jar,lib/jackson-databind-2.8.8.1.jar,lib/jackson-module-scala_2.11-2.8.8.jar,lib/joda-convert-1.6.jar,lib/joda-time-2.9.7.jar,lib/jsr166e-1.1.0.jar,lib/kafka_2.11-0.10.2.1.jar,lib/kafka-clients-0.10.2.1.jar,lib/log4j-1.2.17.jar,lib/metrics-core-3.0.2.jar,lib/metrics-core-2.2.0.jar,lib/netty-all-4.1.11.Final.jar,lib/slf4j-api-1.7.16.jar,lib/slf4j-log4j12-1.7.24.jar,lib/spark-cassandra-connector_2.11-2.0.2.jar,lib/spark-core_2.11-2.1.1.jar,lib/spark-network-common_2.11-2.1.1.jar,lib/spark-sql_2.11-2.1.1.jar,lib/spark-streaming_2.11-2.1.1.jar,lib/spark-streaming-kafka-0-10_2.11-2.1.1.jar,lib/spark-tags_2.11-2.1.1.jar \
    spark-consumer.jar 