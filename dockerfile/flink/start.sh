#!/bin/bash

/opt/flink/bin/start-local.sh

sleep 2

/opt/flink/bin/flink run --class com.smack.flink.KafkaNewConsumer \
--classpath file:////opt/flink-app/lib/avro-1.8.2.jar \
--classpath file:////opt/flink-app/lib/commons-collections-3.2.2.jar \
--classpath file:////opt/flink-app/lib/commons-configuration-1.10.jar \
--classpath file:////opt/flink-app/lib/commons-lang-2.6.jar \
--classpath file:////opt/flink-app/lib/commons-lang3-3.5.jar \
--classpath file:////opt/flink-app/lib/commons-logging-1.2.jar \
--classpath file:////opt/flink-app/lib/commons-math3-3.6.1.jar \
--classpath file:////opt/flink-app/lib/config-1.3.0.jar \
--classpath file:////opt/flink-app/lib/converter-moshi-2.3.0.jar \
--classpath file:////opt/flink-app/lib/flakka-actor_2.11-2.3-custom.jar \
--classpath file:////opt/flink-app/lib/flakka-remote_2.11-2.3-custom.jar \
--classpath file:////opt/flink-app/lib/flakka-slf4j_2.11-2.3-custom.jar \
--classpath file:////opt/flink-app/lib/flink-annotations-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-clients_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-connector-cassandra_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-connector-kafka-0.9_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-connector-kafka-base_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-core-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-java-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-metrics-core-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-optimizer_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-runtime_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-streaming-java_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-streaming-scala_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/flink-scala_2.11-1.2.1.jar \
--classpath file:////opt/flink-app/lib/grizzled-slf4j_2.11-1.3.1.jar \
--classpath file:////opt/flink-app/lib/guava-19.0.jar \
--classpath file:////opt/flink-app/lib/hadoop-auth-2.8.0.jar \
--classpath file:////opt/flink-app/lib/hadoop-common-2.8.0.jar \
--classpath file:////opt/flink-app/lib/hadoop-mapreduce-client-core-2.8.0.jar \
--classpath file:////opt/flink-app/lib/influxdb-java-2.6.jar \
--classpath file:////opt/flink-app/lib/jackson-annotations-2.8.8.jar \
--classpath file:////opt/flink-app/lib/jackson-core-2.8.8.jar \
--classpath file:////opt/flink-app/lib/jackson-databind-2.8.8.1.jar \
--classpath file:////opt/flink-app/lib/jackson-module-scala_2.11-2.8.8.jar \
--classpath file:////opt/flink-app/lib/jsr166e-1.1.0.jar \
--classpath file:////opt/flink-app/lib/kafka_2.11-0.10.2.1.jar \
--classpath file:////opt/flink-app/lib/kafka-clients-0.9.0.1.jar \
--classpath file:////opt/flink-app/lib/kryo-2.24.0.jar \
--classpath file:////opt/flink-app/lib/log4j-1.2.17.jar \
--classpath file:////opt/flink-app/lib/logging-interceptor-3.8.0.jar \
--classpath file:////opt/flink-app/lib/metrics-core-2.2.0.jar \
--classpath file:////opt/flink-app/lib/metrics-core-3.0.2.jar \
--classpath file:////opt/flink-app/lib/moshi-1.4.0.jar \
--classpath file:////opt/flink-app/lib/netty-3.10.6.Final.jar \
--classpath file:////opt/flink-app/lib/netty-all-4.1.11.Final.jar \
--classpath file:////opt/flink-app/lib/objenesis-2.5.1.jar \
--classpath file:////opt/flink-app/lib/okhttp-3.8.0.jar \
--classpath file:////opt/flink-app/lib/okio-1.13.0.jar \
--classpath file:////opt/flink-app/lib/retrofit-2.3.0.jar \
--classpath file:////opt/flink-app/lib/scopt_2.11-3.2.0.jar \
--classpath file:////opt/flink-app/lib/slf4j-api-1.7.16.jar \
--classpath file:////opt/flink-app/lib/slf4j-log4j12-1.7.24.jar \
//opt/flink-app/flink-consumer.jar