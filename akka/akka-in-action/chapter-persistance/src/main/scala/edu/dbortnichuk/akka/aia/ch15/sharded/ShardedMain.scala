package edu.dbortnichuk.akka.aia.ch15.sharded

import akka.actor._
import edu.dbortnichuk.akka.aia.ch15.rest.ShoppersServiceSupport

object ShardedMain extends App with ShoppersServiceSupport {
  implicit val system = ActorSystem("shoppers")

  val shoppers = system.actorOf(ShardedShoppers.props,
    ShardedShoppers.name)

  startService(shoppers)
}
