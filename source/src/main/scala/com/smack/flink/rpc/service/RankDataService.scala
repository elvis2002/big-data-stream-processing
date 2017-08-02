package com.smack.flink.rpc.service

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import com.smack.util.JsonUtil
import com.smack.model.CmdMessage
import com.smack.model.CmdMessageByPeriod

import scala.collection.mutable.HashMap

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Cluster.Builder

import java.util.Date
import java.text.SimpleDateFormat

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala.DataSet
import org.apache.flink.api.java.tuple.Tuple3
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.api.java.tuple.Tuple4
import org.apache.flink.api.java.tuple.Tuple5
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

class RankDataService {

  def handlerAvgTime(cmdMsg: CmdMessageByPeriod): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    //env.setParallelism(1)
    val sql = "select namespace, servername, upstreamresponsetime, create_time from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple4[String, String, Double, Date]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))

    cmdMsg.format match {
      case "day" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, Double, Date], Tuple4[String, String, Double, String]] {
          def map(log: Tuple4[String, String, Double, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'upstreamresponsetime, 'create_time)
        val result: Table = tableEnv.sql("select count(1), sum(upstreamresponsetime), max(upstreamresponsetime), min(upstreamresponsetime), create_time from Customers group by create_time")
        val dataSet = tableEnv.toDataSet[Tuple5[Long, Double, Double, Double, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          tmap.put("totaltime", x.f1.toString())
          tmap.put("maxtime", x.f2.toString())
          tmap.put("mintime", x.f3.toString())
          map.put(x.f4.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "hour" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, Double, Date], Tuple4[String, String, Double, String]] {
          def map(log: Tuple4[String, String, Double, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'upstreamresponsetime, 'create_time)
        val result: Table = tableEnv.sql("select count(1), sum(upstreamresponsetime), max(upstreamresponsetime), min(upstreamresponsetime), create_time from Customers group by create_time")
        val dataSet1 = tableEnv.toDataSet[Tuple5[Long, Double, Double, Double, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet1.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          tmap.put("totaltime", x.f1.toString())
          tmap.put("maxtime", x.f2.toString())
          tmap.put("mintime", x.f3.toString())
          map.put(x.f4.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "minute" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, Double, Date], Tuple4[String, String, Double, String]] {
          def map(log: Tuple4[String, String, Double, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'upstreamresponsetime, 'create_time)
        val result: Table = tableEnv.sql("select count(1), sum(upstreamresponsetime), max(upstreamresponsetime), min(upstreamresponsetime), create_time from Customers group by create_time")
        val dataSet2 = tableEnv.toDataSet[Tuple5[Long, Double, Double, Double, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet2.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          tmap.put("totaltime", x.f1.toString())
          tmap.put("maxtime", x.f2.toString())
          tmap.put("mintime", x.f3.toString())
          map.put(x.f4.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case _ =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, Double, Date], Tuple4[String, String, Double, String]] {
          def map(log: Tuple4[String, String, Double, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'upstreamresponsetime, 'create_time)
        val result: Table = tableEnv.sql("select count(1), sum(upstreamresponsetime), max(upstreamresponsetime), min(upstreamresponsetime) from Customers")
        val dataSet3 = tableEnv.toDataSet[Tuple4[Long, Double, Double, Double]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet3.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          tmap.put("totaltime", x.f1.toString())
          tmap.put("maxtime", x.f2.toString())
          tmap.put("mintime", x.f3.toString())
          map.put("data", tmap)
        })
        JsonUtil.toJsonStrForMap(map)
    }

  }

  def handlerUv(cmdMsg: CmdMessageByPeriod): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val sql = "select namespace, servername, remoteaddr, create_time from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple4[String, String, String, Date]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))
    cmdMsg.format match {
      case "day" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, String, Date], Tuple4[String, String, String, String]] {
          def map(log: Tuple4[String, String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'remoteaddr, 'create_time)
        val result: Table = tableEnv.sql("select create_time, count(remoteaddr) from Customers group by create_time, remoteaddr")
        val dataSet2 = tableEnv.toDataSet[Tuple2[String, Long]](result)
        var tepmap = new HashMap[String, Long]
        dataSet2.collect().foreach(x => {
          if (tepmap.get(x.f0).isEmpty) {
            tepmap.put(x.f0, 1)
          } else {
            val tepv = tepmap.get(x.f0)
            tepmap.put(x.f0, tepv.get + 1)
          }
        })
        var map = new HashMap[String, HashMap[String, String]]
        tepmap.foreach(tep => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", tep._2.toString())
          map.put(tep._1, tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "hour" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, String, Date], Tuple4[String, String, String, String]] {
          def map(log: Tuple4[String, String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'remoteaddr, 'create_time)
        val result: Table = tableEnv.sql("select create_time, count(remoteaddr) from Customers group by create_time, remoteaddr")
        val dataSet2 = tableEnv.toDataSet[Tuple2[String, Long]](result)
        var tepmap = new HashMap[String, Long]
        dataSet2.collect().foreach(x => {
          if (tepmap.get(x.f0).isEmpty) {
            tepmap.put(x.f0, 1)
          } else {
            val tepv = tepmap.get(x.f0)
            tepmap.put(x.f0, tepv.get + 1)
          }
        })
        var map = new HashMap[String, HashMap[String, String]]
        tepmap.foreach(tep => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", tep._2.toString())
          map.put(tep._1, tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "minute" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, String, Date], Tuple4[String, String, String, String]] {
          def map(log: Tuple4[String, String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'remoteaddr, 'create_time)
        val result: Table = tableEnv.sql("select create_time, count(remoteaddr) from Customers group by create_time, remoteaddr")
        val dataSet2 = tableEnv.toDataSet[Tuple2[String, Long]](result)

        var tepmap = new HashMap[String, Long]
        dataSet2.collect().foreach(x => {
          if (tepmap.get(x.f0).isEmpty) {
            tepmap.put(x.f0, 1)
          } else {
            val tepv = tepmap.get(x.f0)
            tepmap.put(x.f0, tepv.get + 1)
          }
        })
        var map = new HashMap[String, HashMap[String, String]]
        tepmap.foreach(tep => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", tep._2.toString())
          map.put(tep._1, tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case _ =>
        val newInputDS = inputDS.map(new MapFunction[Tuple4[String, String, String, Date], Tuple4[String, String, String, String]] {
          def map(log: Tuple4[String, String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f3)
            new Tuple4(log.f0, log.f1, log.f2, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'remoteaddr, 'create_time)
        val result: Table = tableEnv.sql("select count(1) from Customers group by remoteaddr")
        val dataSet3 = tableEnv.toDataSet[Long](result)
        var totalnum = 0l
        dataSet3.collect().foreach(x => {
          totalnum = totalnum + 1
        })
        var map = new HashMap[String, HashMap[String, String]]
        var tmap = new HashMap[String, String]
        tmap.put("totalnum", totalnum.toString())
        map.put("data", tmap)
        JsonUtil.toJsonStrForMap(map)
    }
  }

  def handlerPv(cmdMsg: CmdMessageByPeriod): String = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val sql = "select namespace, servername, create_time  from mydb.nginx_base_log where namespace='" + cmdMsg.namespace + "' and servername='" + cmdMsg.serverName + "' and create_time>'" + cmdMsg.start_time + "' and create_time<='" + cmdMsg.end_time + "' "
    val inputDS = env
      .createInput(new CassandraInputFormat[Tuple3[String, String, Date]](sql, new ClusterBuilder() {
        def buildCluster(builder: Cluster.Builder): Cluster = {
          builder.addContactPoint("cassandra").withPort(9042).build()
        }
      }))

    cmdMsg.format match {
      case "day" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple3[String, String, Date], Tuple3[String, String, String]] {
          def map(log: Tuple3[String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd");
            val create_time = sdf.format(log.f2)
            new Tuple3(log.f0, log.f1, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'create_time)
        val result: Table = tableEnv.sql("select count(1), create_time from Customers group by create_time")
        val dataSet2 = tableEnv.toDataSet[Tuple2[Long, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet2.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          map.put(x.f1.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "hour" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple3[String, String, Date], Tuple3[String, String, String]] {
          def map(log: Tuple3[String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            val create_time = sdf.format(log.f2)
            new Tuple3(log.f0, log.f1, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'create_time)
        val result: Table = tableEnv.sql("select count(1), create_time from Customers group by create_time")
        val dataSet2 = tableEnv.toDataSet[Tuple2[Long, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet2.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          map.put(x.f1.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case "minute" =>
        val newInputDS = inputDS.map(new MapFunction[Tuple3[String, String, Date], Tuple3[String, String, String]] {
          def map(log: Tuple3[String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f2)
            new Tuple3(log.f0, log.f1, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'create_time)
        val result: Table = tableEnv.sql("select count(1), create_time from Customers group by create_time")
        val dataSet2 = tableEnv.toDataSet[Tuple2[Long, String]](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet2.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.f0.toString())
          map.put(x.f1.toString(), tmap)
        })
        JsonUtil.toJsonStrForMap(map)
      case _ =>
        val newInputDS = inputDS.map(new MapFunction[Tuple3[String, String, Date], Tuple3[String, String, String]] {
          def map(log: Tuple3[String, String, Date]) = {
            val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            val create_time = sdf.format(log.f2)
            new Tuple3(log.f0, log.f1, create_time)
          }
        })
        tableEnv.registerDataSet("Customers", newInputDS, 'namespace, 'servername, 'create_time)
        val result: Table = tableEnv.sql("select count(1) from Customers")
        val dataSet3 = tableEnv.toDataSet[Long](result)
        var map = new HashMap[String, HashMap[String, String]]
        dataSet3.collect().foreach(x => {
          var tmap = new HashMap[String, String]
          tmap.put("totalnum", x.toString())
          map.put("data", tmap)
        })
        JsonUtil.toJsonStrForMap(map)
    }
  }
}