package com.smack.flink.rpc.actor

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import com.smack.flink.rpc.service.DataService
import com.smack.flink.rpc.service.RankDataService
import com.smack.util.JsonUtil

import com.smack.model.CmdMessage
import com.smack.model.CmdMessageByPeriod

class RpcActor extends Actor with ActorLogging {
  val dataDealService = new DataService()
  val rankDataDealService = new RankDataService()
  def receive = {
    case cmdMsg: CmdMessage => {
      cmdMsg.cmd match {
        case "avgtime" =>
          sender ! dataDealService.handlerAvgTime(cmdMsg)
        case "pv" =>
          sender ! dataDealService.handlerPv(cmdMsg)
        case "uv" =>
          sender ! dataDealService.handlerUv(cmdMsg)
        case _ =>
          sender ! JsonUtil.toJsonStr(Map("message" -> "not found cmd"))
      }
    }
    case cmdMsgeGroup: CmdMessageByPeriod => {
      cmdMsgeGroup.cmd match {
        case "avgtime" =>
          sender ! rankDataDealService.handlerAvgTime(cmdMsgeGroup)
        case "pv" =>
          sender ! rankDataDealService.handlerPv(cmdMsgeGroup)
        case "uv" =>
          sender ! rankDataDealService.handlerUv(cmdMsgeGroup)
        case _ =>
          sender ! JsonUtil.toJsonStr(Map("message" -> "not found cmd"))
      }
    }
    case x => log.info("xxxx" + x)
  }
}