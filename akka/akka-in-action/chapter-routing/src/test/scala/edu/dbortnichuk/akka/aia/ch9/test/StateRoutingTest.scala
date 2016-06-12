package edu.dbortnichuk.akka.aia.ch9.test

import akka.actor._
import akka.testkit._
import edu.dbortnichuk.akka.aia.ch9.{RouteStateOff, RouteStateOn, SwitchRouter, SwitchRouter2}
import org.scalatest._

import scala.concurrent.duration._

class StateRoutingTest
  extends TestKit(ActorSystem("StateRoutingTest"))
  with WordSpecLike with BeforeAndAfterAll {

  override def afterAll() {
    system.terminate
  }

  "The Router" must {
    "routes depending on state" in {
      //<start id="ch09-routing-state-test"/>
      val normalFlowProbe = TestProbe()
      val cleanupProbe = TestProbe()
      val router = system.actorOf(
        Props(new SwitchRouter(
          normalFlow = normalFlowProbe.ref,
          cleanUp = cleanupProbe.ref)))

      val msg = "message"
      router ! msg

      cleanupProbe.expectMsg(msg)
      normalFlowProbe.expectNoMsg(1 second)

      router ! RouteStateOn //<co id="ch09-routing-state-test-1" />

      router ! msg

      cleanupProbe.expectNoMsg(1 second)
      normalFlowProbe.expectMsg(msg)

      router ! RouteStateOff //<co id="ch09-routing-state-test-2" />
      router ! msg

      cleanupProbe.expectMsg(msg)
      normalFlowProbe.expectNoMsg(1 second)

      //<end id="ch09-routing-state-test"/>
    }
    "routes2 depending on state" in {
      //<start id="ch09-routing-state-test2"/>
      val normalFlowProbe = TestProbe()
      val cleanupProbe = TestProbe()
      val router = system.actorOf(
        Props(new SwitchRouter2(
          normalFlow = normalFlowProbe.ref,
          cleanUp = cleanupProbe.ref)))

      val msg = "message"
      router ! msg

      cleanupProbe.expectMsg(msg)
      normalFlowProbe.expectNoMsg(1 second)

      router ! RouteStateOn //<co id="ch09-routing-state-test2-1" />

      router ! msg

      cleanupProbe.expectNoMsg(1 second)
      normalFlowProbe.expectMsg(msg)

      router ! RouteStateOff //<co id="ch09-routing-state-test2-2" />
      router ! msg

      cleanupProbe.expectMsg(msg)
      normalFlowProbe.expectNoMsg(1 second)

      //<end id="ch09-routing-state-test2"/>
    }
    "log wrong statechange requests" in {
      //<start id="ch09-routing-state-test3"/>
      val normalFlowProbe = TestProbe()
      val cleanupProbe = TestProbe()
      val router = system.actorOf(
        Props(new SwitchRouter(
          normalFlow = normalFlowProbe.ref,
          cleanUp = cleanupProbe.ref)))

      router ! new RouteStateOff()

      router ! RouteStateOn

      router ! RouteStateOn

      //<end id="ch09-routing-state-test3"/>
    }
  }
}