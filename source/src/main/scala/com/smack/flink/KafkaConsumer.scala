package com.smack.flink

import java.util.Properties
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction
import java.util.List
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.connectors.cassandra.CassandraTupleSink
import org.apache.flink.streaming.connectors.cassandra.ClusterBuilder
import com.datastax.driver.core.Cluster;
import java.util.UUID
import org.apache.flink.api.java.tuple.Tuple4;

object KafkaConsumer extends App {
	val topics = "test"
	val brokers = "10.213.57.109:11092";
	val cassandraHost = "172.17.0.4"
	val group = "con-consumer-group"
	val sec = "_ok_"

	val env = StreamExecutionEnvironment.getExecutionEnvironment
	env.enableCheckpointing(5000)
	val properties = new Properties()
	properties.setProperty("bootstrap.servers", brokers)
	properties.setProperty("group.id", group)

	val stream = env.addSource(new FlinkKafkaConsumer09(topics, new SimpleStringSchema(), properties))
	val s1 = stream.map(new MapFunction[String, Tuple4[UUID, String, String, String]] {
		def map(value: String) = {
			val temp = value.split("_ok_")
			if (temp.size > 3) {
				new Tuple4(UUID.fromString(temp(0)), temp(1), temp(2), temp(3));
			} else {
				new Tuple4(UUID.randomUUID(), "default", "default", value);
			}
		}
	})

	//s1.print()

	s1.addSink(new CassandraTupleSink[Tuple4[UUID, String, String, String]](
		"INSERT INTO mydb.event"
			+ " (id, namespace, servername, log)"
			+ " VALUES (?, ?, ?, ?);",
		new ClusterBuilder() {
			def buildCluster(builder: Cluster.Builder): Cluster = {
				builder.addContactPoint(cassandraHost).withPort(9042).build()
			}
		}));
	env.execute()
}
