package edu.dbortnichuk.akka.aia.ch10

import akka.actor.Actor

class EchoActor extends Actor {
  def receive = {
    case msg: AnyRef =>
      sender() ! msg

  }
}
