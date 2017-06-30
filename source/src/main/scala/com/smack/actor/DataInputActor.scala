package com.smack.actor

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import akka.actor.DeadLetter
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.cluster.routing.ClusterRouterPool
import akka.routing.ConsistentHashingPool
import akka.cluster.routing.ClusterRouterPoolSettings
import akka.io.IO
import spray.can.Http
import com.smack.actor.http.DataService
import com.typesafe.config.Config
import com.smack.actor.router.route.InputWorker

class DataInputActor(config: Config) extends Actor with ActorLogging {

	val workerRouter = context.actorOf(
		ClusterRouterPool(ConsistentHashingPool(0), ClusterRouterPoolSettings(
			totalInstances = 40, maxInstancesPerNode = 1,
			allowLocalRoutees = true, useRole = None)).props(Props(new InputWorker(config))),
		name = "workerInRouter")
		
	val handler = context.actorOf(Props(new DataService(workerRouter)), name = "handler")
	implicit val system = context.system
	IO(Http) ! Http.Bind(handler, interface = config.getString("http.interface"), port = config.getInt("http.port"))

	override val supervisorStrategy =
		OneForOneStrategy() {
            case e: NullPointerException =>
                log.error("[{}] create NullPointerException [{}]", self, e.getLocalizedMessage()); Restart
            case e: Exception =>
                log.error("[{}] create NullPointerException [{}]", self, e.getLocalizedMessage()); Restart
        }
	def receive = {
		case x => log.info("x=" + x)
	}
}