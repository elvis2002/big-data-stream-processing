package com.smack.cassandra

import com.smack.cassandra.Connector._
import com.outworkers.phantom.dsl._

class EventDatabase(override val connector: KeySpaceDef) extends Database[EventDatabase](connector) {

	object eventModel extends ConcreteEventModel with connector.Connector

}

object ProductionDb extends EventDatabase(connector)

trait ProductionDatabaseProvider {
	def database: EventDatabase
}

trait ProductionDatabase extends ProductionDatabaseProvider {
	override val database = ProductionDb
}