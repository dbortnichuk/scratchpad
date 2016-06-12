package edu.dbortnichuk.akka.aia.ch6

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object BackendRemoteDeployMain extends App {
  val config = ConfigFactory.load("backend")
  val system = ActorSystem("backend", config)
}
