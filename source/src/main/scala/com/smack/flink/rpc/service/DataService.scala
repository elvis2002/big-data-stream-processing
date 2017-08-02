package com.smack.flink.rpc.service

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import com.smack.util.JsonUtil
import com.smack.model.CmdMessage

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Cluster.Builder

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala.DataSet
import org.apache.flink.api.java.tuple.Tuple3
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.batch.connectors.cassandra.CassandraInputFormat
import org.apache.flink.streaming.connectors.cassandra.ClusterBuilder
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction
import org.apache.flink.api.common.typeinfo.TypeHint
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.{ Table, TableEnvironment }
import org.apache.flink.table.api.scala._

class DataService {

  def handlerAvgTime(cmdMsg: CmdMessage): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    //env.setParallelism(1)
    val sql = "select namespace, servername, upstreamresponsetime from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple3[String, String, Double]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))

    tableEnv.registerDataSet("Customers", inputDS, 'namespace, 'servername, 'upstreamresponsetime)

    val result: Table = tableEnv.sql("select count(1), sum(upstreamresponsetime) from Customers")

    val dataSet = tableEnv.toDataSet[Tuple2[Long, Double]](result)

    var totalnum = 0L
    var totaltime = 0.0
    dataSet.collect().foreach(x => {
      totalnum = totalnum + x.f0
      totaltime = totaltime + x.f1
    })

    val map = Map("namespace" -> cmdMsg.namespace, "servername" -> cmdMsg.serverName, "totalnum" -> totalnum.toString(), "totaltime" -> totaltime.toString(), "avgtime" -> (totaltime / totalnum).toString())
    JsonUtil.toJsonStr(map)
  }

  def handlerUv(cmdMsg: CmdMessage): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val sql = "select namespace, servername, remoteaddr from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple3[String, String, String]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))

    tableEnv.registerDataSet("Customers", inputDS, 'namespace, 'servername, 'remoteaddr)

    val result: Table = tableEnv.sql("select count(1) from Customers group by remoteaddr")
    val dataSet = tableEnv.toDataSet[Long](result)
    var totalnum = 0l
    dataSet.collect().foreach(x => {
      totalnum = totalnum + 1
    })
    val map = Map("namespace" -> cmdMsg.namespace, "servername" -> cmdMsg.serverName, "totalnum" -> totalnum.toString())
    JsonUtil.toJsonStr(map)
  }

  def handlerPv(cmdMsg: CmdMessage): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val sql = "select namespace, servername, remoteaddr from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple3[String, String, String]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))

    tableEnv.registerDataSet("Customers", inputDS, 'namespace, 'servername, 'remoteaddr)

    val result: Table = tableEnv.sql("select count(1) from Customers")

    val dataSet = tableEnv.toDataSet[Long](result)

    var totalnum = "0"
    dataSet.collect().foreach(x => {
      totalnum = x.toString()
    })

    val map = Map("namespace" -> cmdMsg.namespace, "servername" -> cmdMsg.serverName, "totalnum" -> totalnum)
    JsonUtil.toJsonStr(map)
  }
}