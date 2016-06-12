package edu.dbortnichuk.akka.aia.ch10.mjvm

// start with multi-jvm:test-only aia.channels.ReliableProxySampleSpec

import akka.actor.{Actor, Props}
import akka.testkit.ImplicitSender
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}


/**
 * Hooks up MultiNodeSpec with ScalaTest
 */
//<start id="ch09-proxy-config-test"/>
import akka.remote.testkit.{MultiNodeConfig, MultiNodeSpec, MultiNodeSpecCallbacks}

trait STMultiNodeSpec
  extends MultiNodeSpecCallbacks
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  override def beforeAll() = multiNodeSpecBeforeAll()

  override def afterAll() = multiNodeSpecAfterAll()
}


object ReliableProxySampleConfig extends MultiNodeConfig {
  val client = role("Client")                              //<co id="ch09-proxy-config-test-1"/>
  val server = role("Server")                              //<co id="ch09-proxy-config-test-2"/>
  testTransport(on = true)                                 //<co id="ch09-proxy-config-test-3"/>
}

class ReliableProxySampleSpecMultiJvmNode1 extends ReliableProxySample
class ReliableProxySampleSpecMultiJvmNode2 extends ReliableProxySample

//<end id="ch09-proxy-config-test"/>

//<start id="ch09-proxy-test"/>
import akka.contrib.pattern.ReliableProxy
import akka.remote.transport.ThrottlerTransportAdapter.Direction

import scala.concurrent.Await
import scala.concurrent.duration._


class ReliableProxySample
  extends MultiNodeSpec(ReliableProxySampleConfig)
  with STMultiNodeSpec
  with ImplicitSender {

  import ReliableProxySampleConfig._

  def initialParticipants = roles.size

  "A MultiNodeSample" must {

    "wait for all nodes to enter a barrier" in {
      enterBarrier("startup")
    }

    "send to and receive from a remote node" in {
      runOn(client) {
        enterBarrier("deployed")
        val pathToEcho = node(server) / "user" / "echo"
        val echo = system.actorSelection(pathToEcho)    //<co id="ch09-proxy-test-1"/>
        val proxy = system.actorOf(
          ReliableProxy.props(pathToEcho, 500.millis), "proxy")        //<co id="ch09-proxy-test-2"/>

        proxy ! "message1"                                            //<co id="ch09-proxy-test-3"/>
        expectMsg("message1")
        Await.ready(
          testConductor.blackhole( client, server, Direction.Both),    //<co id="ch09-proxy-test-4"/>
          1 second)

        echo ! "DirectMessage"                                         //<co id="ch09-proxy-test-5"/>
        proxy ! "ProxyMessage"
        expectNoMsg(3 seconds)

        Await.ready(
          testConductor.passThrough( client, server, Direction.Both),  //<co id="ch09-proxy-test-6"/>
          1 second)

        expectMsg("ProxyMessage")                                      //<co id="ch09-proxy-test-7"/>

        echo ! "DirectMessage2"                                        //<co id="ch09-proxy-test-8"/>
        expectMsg("DirectMessage2")
      }

      runOn(server) {
        system.actorOf(Props(new Actor {                               //<co id="ch09-proxy-test-9"/>
          def receive = {
            case msg: AnyRef => {
              sender() ! msg
            }
          }
        }), "echo")
        enterBarrier("deployed")
      }

      enterBarrier("finished")
    }
  }
}
//<end id="ch09-proxy-test"/>
