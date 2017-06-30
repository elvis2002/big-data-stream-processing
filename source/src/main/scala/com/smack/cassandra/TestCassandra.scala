package com.smack.cassandra

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import com.smack.util.JsonUtil

object TestCassandra extends App {
//	val result = EventService.getEventById(UUID.fromString("4d5a8173-3cb9-45c8-af20-ecf0f7ecb846"))
//
//	result onComplete {
//		case Success(None) => print("not found")
//		case Success(event) => for (evt <- event) println(evt)
//		case _ => print("error")
//	}
	
	val result = EventService.getListByserverName("test","a1",10)
	result onComplete {
		case Success(eventList) => print(JsonUtil.toJson(eventList))
		case _ => print("error")
	}
}