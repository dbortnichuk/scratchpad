package edu.dbortnichuk.akka.aia.ch4.test

import akka.actor._
import akka.testkit.TestKit
import edu.dbortnichuk.akka.aia.ch4.LifeCycleHooks
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class LifeCycleHooksTest extends TestKit(ActorSystem("LifCycleTest")) with WordSpecLike with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
  }

  "The Child" must {
    "log lifecycle hooks" in {
      //<start id="ch3-life-test"/>
      val testActorRef = system.actorOf( //<co id="ch3-life-test-start" />
        Props[LifeCycleHooks], "LifeCycleHooks")
      testActorRef ! "restart" //<co id="ch3-life-test-restart" />
      testActorRef.tell("msg", testActor)
      expectMsg("msg")
      system.stop(testActorRef) //<co id="ch3-life-test-stop" />
      Thread.sleep(1000)
      //<end id="ch3-life-test"/>

    }
  }
}