package com.dbortnichu.akka

import akka.actor._
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val system = ActorSystem("actors")

  val actor1 = system.actorOf(Props[MyActor], "node_1")
  val actor2 = system.actorOf(Props[MyActor], "node_2")

  actor2 ! "ping_other"

  system.awaitTermination()

  Console.println("Bye!")
}

class MyActor extends Actor with ActorLogging {
  import akka.pattern.ask

  implicit val timeout = Timeout(100.days)

  def receive = {
    case "ping_other" => {
      val selection = context.actorSelection("../node_1")
      log.info("Sending ping to node_1")
      val future: Future[String] = ask(selection, "ping").mapTo[String]
      future.onSuccess {
        case result : String ⇒ log.info("Got result " + result)
      }
    }
    case "ping" => {
      log.info("Sending back pong!")
      sender ! "pong"
    }
  }
}