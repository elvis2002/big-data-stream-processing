package com.smack.cassandra

import java.util.UUID
import java.util.Date

case class Event(
	id: UUID,
	namespace: String,
	servername: String,
	log: String
)
