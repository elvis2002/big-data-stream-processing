package com.smack.cassandra

import com.outworkers.phantom.connectors.{ CassandraConnection, ContactPoint, ContactPoints }
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConversions._

object Connector {
	private val config = ConfigFactory.load("data-input.conf")

	private val hosts = config.getStringList("cassandra.host")
	private val keyspace = config.getString("cassandra.keyspace")
	private val username = config.getString("cassandra.username")
	private val password = config.getString("cassandra.password")

	
	lazy val connector: CassandraConnection = ContactPoints(hosts)
		.withClusterBuilder(_.withCredentials(username, password))
		.keySpace(keyspace)
}