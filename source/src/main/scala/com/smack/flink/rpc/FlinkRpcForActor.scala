package com.smack.flink.rpc

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.actor.DeadLetter
import com.smack.flink.rpc.actor.RpcActor
import org.apache.flink.streaming.api.scala._
import java.util.Properties
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object FlinkRpcForActor extends App {
	var host = "0.0.0.0"
	if (!args.isEmpty) {
		host = args(0)
	} else {
		System.out.println("need start parameter !")
		System.exit(0)
	}
	System.setProperty("akka.remote.netty.tcp.hostname", host)
	System.setProperty("akka.remote.netty.tcp.port", "12551")
	val config = ConfigFactory.load("akka.conf")
	implicit val system = ActorSystem("RpcForActor", config)

	val t = system.actorOf(Props[RpcActor], "rpcActor")
	System.out.println(t)

	System.out.println("Starting Flink Rpc func !")

	val topics = "testrpc"
	val brokers = "kafka:9092";
	val group = "con-consumer-group"
	val env = StreamExecutionEnvironment.getExecutionEnvironment
	env.enableCheckpointing(5000)
	val properties = new Properties()
	properties.setProperty("bootstrap.servers", brokers)
	properties.setProperty("group.id", group)
	val stream = env.addSource(new FlinkKafkaConsumer09(topics, new SimpleStringSchema(), properties))
	stream.print()
	env.execute()
}