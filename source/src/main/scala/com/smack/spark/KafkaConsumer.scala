package com.smack.spark

import java.util.Properties
import _root_.kafka.serializer.StringDecoder

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies

import com.datastax.spark.connector.{ SomeColumns, _ }
import org.apache.spark.{ SparkConf, SparkContext }
import com.datastax.spark.connector.streaming._

object KafkaConsumer extends App {
	val topics = "test"
	val brokers = "10.213.57.109:11092";
	val cassandraHost = "172.17.0.4"
	val group = "con-consumer-group"

	// Create context with 10 second batch interval
	val sparkConf = new SparkConf(true)
		.set("spark.cassandra.connection.host", cassandraHost)
		.set("spark.cleaner.ttl", "3600")
		.setMaster("local[4]")
		.setAppName("KafkAnalysis")
	val ssc = new StreamingContext(sparkConf, Seconds(10))

	val topicsSet = topics.split(",").toSet
	val kafkaParams = Map(
		"bootstrap.servers" -> brokers,
		"group.id" -> group,
		"key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
		"value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
		"auto.offset.reset" -> "latest",
		"enable.auto.commit" -> (true: java.lang.Boolean));

	val messages = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

	messages.map(s => s.value()).map(x => x.split("_ok_")).filter { x => x.size > 3 }.map { x => (x(0).toString(), x(1).toString(), x(2).toString(), x(3).toString()) }.saveToCassandra("mydb", "event", SomeColumns("id", "namespace", "servername", "log"))

	ssc.start()
	ssc.awaitTermination()
}
