package edu.dbortnichuk.akka.aia.ch6.mjvm

import akka.remote.testkit.MultiNodeConfig

object ClientServerConfig extends MultiNodeConfig {
  val frontend = role("frontend")
  val backend = role("backend")
}
