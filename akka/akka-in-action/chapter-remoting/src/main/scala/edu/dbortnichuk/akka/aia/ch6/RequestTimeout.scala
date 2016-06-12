package edu.dbortnichuk.akka.aia.ch6

import akka.util.Timeout
import com.typesafe.config.Config

trait RequestTimeout {
  import scala.concurrent.duration._
  def configuredRequestTimeout(config: Config): Timeout = { //<co id="ch02_timeout_spray_can"/>
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
