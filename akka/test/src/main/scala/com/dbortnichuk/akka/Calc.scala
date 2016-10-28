package com.dbortnichuk.akka

import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, DeadLetter, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy, Terminated}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dbort on 27.10.2016.
  */
object Calc {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("actors")

    val driver = system.actorOf(Props[Driver], "driver")
    val worker = system.actorOf(Props[Worker], "worker")
    val watcher = system.actorOf(Props(new Watcher(worker)), "watcher")

    system.eventStream.subscribe(watcher, classOf[DeadLetter])

    //system.eventStream.publish(Task(2, -3, Sum))

    driver ! Task(1, 3, Sum)
    Thread.sleep(100)
    worker ! ISIssue
    Thread.sleep(100)
    driver ! Task(1, 0, Div)

    system.awaitTermination()

    Console.println("Bye!")
  }
}

class Driver extends Actor with ActorLogging {

  import akka.pattern.ask

  implicit val timeout = Timeout(100.days)

  def receive = {
    case task: Task => {
      val worker = context.actorSelection("../worker")

      //worker.forward(task)
      //log.info("Sending ping to node_1")
      val future: Future[Result] = ask(worker, task).mapTo[Result]
      future.onSuccess {
        case Result(res) ⇒ log.info("Got result " + res)
      }
    }
  }
}

class Worker extends Actor with ActorLogging {



  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    println("preStart")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    println("postStop")
  }

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println("preRestart")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println("postRestart")
  }


  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException => Restart
    case _: IllegalStateException => Stop
    case _: IllegalAccessException => Resume
  }

  def receive = {

    case Task(term1, term2, operation) => {
      //log.info("Sending back pong!")
      operation match {
        case Sum => sender ! Result(term1 + term2)
        case Div => sender ! Result(term1 / term2)
      }
      self ! PoisonPill
    }
    case IAIssue => throw new IllegalArgumentException("issue occured")
    case ISIssue => throw new IllegalStateException("issue occured")
    case IAcIssue => throw new IllegalAccessException("issue occured")
  }
}

class Watcher(workerRef: ActorRef) extends Actor {
  context.watch(workerRef)
  def receive = {
    case t: Task => println(t)
    case Terminated(actorRef) => println(s"terminated $actorRef")
    case DeadLetter(msg, from, to) => println(s"failed to deliver msg: ${msg} from: ${from.path.name} to: ${to.path.name}")
  }
}

case class Task(term1: Double, term2: Double, operation: Operation)

case class Result(res: Double)

case object IAIssue
case object ISIssue
case object IAcIssue

sealed trait Operation

case object Sum extends Operation

case object Div extends Operation


