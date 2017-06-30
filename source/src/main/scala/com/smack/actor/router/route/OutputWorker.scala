package com.smack.actor.router.route

import akka.actor.Actor
import akka.actor.ActorLogging
import scala.collection.JavaConversions._
import com.smack.model.QueryEvent
import com.typesafe.config.Config
import com.smack.cassandra.EventService
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorRef
import com.smack.util.JsonUtil

class OutputWorker(config: Config) extends Actor with ActorLogging {

	def receive = {
		case evt: QueryEvent =>
			log.info(evt.toString())
			deal(evt, sender())
		case x => log.info("OutputWorker=" + x)
	}

	def deal(evt: QueryEvent, actor: ActorRef) {
		try {
			val result = EventService.getListByserverName(evt.namespace, evt.serverName, 10)
			result onComplete {
				case Success(eventList) => actor ! JsonUtil.toJson(eventList)
				case _ => print("error")
			}
		} catch {
			case t: Throwable => log.info(t.getLocalizedMessage)
		}

	}
}