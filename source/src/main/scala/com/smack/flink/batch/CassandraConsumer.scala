package com.smack.flink.stream

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.batch.connectors.cassandra.CassandraInputFormat
import org.apache.flink.streaming.connectors.cassandra.ClusterBuilder

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Cluster.Builder

import java.util.ArrayList

object CassandraConsumer extends App {

  val env = ExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)

  val inputDS = env
    .createInput(new CassandraInputFormat[Tuple2[String, String]]("select namespace, servername from mydb.nginx_base_log", new ClusterBuilder() {
      def buildCluster(builder: Cluster.Builder): Cluster = {
        builder.addContactPoint("cassandra").withPort(9042).build()
      }
    }))
  inputDS.print()
  
  
  
}