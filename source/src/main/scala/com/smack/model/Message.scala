package com.smack.model

case class MsgEvent(namespace: String, serverName: String, msg: String)

case class QueryEvent(namespace: String, serverName: String, start: Int, offset: Int)