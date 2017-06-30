package com.smack.cassandra

import com.outworkers.phantom.dsl._

import scala.concurrent.Future

trait EventService extends ProductionDatabase {

	def getEventById(id: UUID): Future[Option[Event]] = {
		database.eventModel.getByEventId(id)
	}

	def getListByserverName(namespace: String, serverName: String, num: Integer): Future[List[Event]] = {
		database.eventModel.getListByCondition(namespace, serverName, num)
	}

	def saveOrUpdate(events: Event): Future[ResultSet] = {
		for {
			byId <- database.eventModel.store(events)
		} yield byId
	}

	def delete(event: Event): Future[ResultSet] = {
		for {
			byID <- database.eventModel.deleteById(event.id)
		} yield byID
	}
}

object EventService extends EventService with ProductionDatabase
