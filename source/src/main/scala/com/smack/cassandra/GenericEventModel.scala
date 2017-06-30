package com.smack.cassandra

import java.util.UUID
import com.outworkers.phantom.dsl._
import scala.concurrent.Future

class EventModel extends CassandraTable[ConcreteEventModel, Event] {

	override def tableName: String = "event"

	object id extends TimeUUIDColumn(this) with PartitionKey {
		override lazy val name = "id"
	}

	object namespace extends StringColumn(this) with PartitionKey

	object servername extends StringColumn(this) with PartitionKey

	object log extends StringColumn(this)
	
	//object date extends DateColumn(this)
	
	override def fromRow(r: Row): Event = Event(id(r), namespace(r), servername(r), log(r))
}

abstract class ConcreteEventModel extends EventModel with RootConnector {

	def getByEventId(id: UUID): Future[Option[Event]] = {
		select
			.where(_.id eqs id)
			.consistencyLevel_=(ConsistencyLevel.ONE)
			.one()
	}
	
	def getListByCondition(namespace: String, servername: String, num: Integer): Future[List[Event]] = {
		select
			.where(_.namespace eqs namespace).and { _.servername eqs servername}
			.consistencyLevel_=(ConsistencyLevel.ONE).allowFiltering()
			.limit(num).fetch()
	}

	def store(event: Event): Future[ResultSet] = {
		insert
			.value(_.id, event.id)
			.value(_.namespace, event.namespace)
			.value(_.servername, event.servername)
			.value(_.log, event.log)
			.consistencyLevel_=(ConsistencyLevel.ONE)
			.future()
	}

	def deleteById(id: UUID): Future[ResultSet] = {
		delete
			.where(_.id eqs id)
			.consistencyLevel_=(ConsistencyLevel.ONE)
			.future()
	}
}
