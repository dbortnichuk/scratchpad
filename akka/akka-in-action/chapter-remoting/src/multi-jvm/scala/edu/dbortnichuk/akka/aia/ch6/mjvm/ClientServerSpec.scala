package edu.dbortnichuk.akka.aia.ch6.mjvm

import akka.actor._
import akka.remote.testkit.MultiNodeSpec
import akka.testkit.ImplicitSender
import akka.util.Timeout
import edu.dbortnichuk.akka.aia.ch6.BoxOffice

import scala.concurrent.duration._

class ClientServerSpecMultiJvmFrontend extends ClientServerSpec
class ClientServerSpecMultiJvmBackend extends ClientServerSpec

class ClientServerSpec extends MultiNodeSpec(ClientServerConfig)
with STMultiNodeSpec with ImplicitSender {

  import ClientServerConfig._

  val backendNode = node(backend)

  def initialParticipants = roles.size

  "A Client Server configured app" must {

    "wait for all nodes to enter a barrier" in {
      enterBarrier("startup")
    }

    "be able to create an event and sell a ticket" in {
      runOn(backend) {
        system.actorOf(BoxOffice.props(Timeout(1 second)), "boxOffice")
        enterBarrier("deployed")
      }

      runOn(frontend) {
        enterBarrier("deployed")

        val path = node(backend) / "user" / "boxOffice"
        val actorSelection = system.actorSelection(path)

        actorSelection.tell(Identify(path), testActor)

        val actorRef = expectMsgPF() {
          case ActorIdentity(`path`, Some(ref)) => ref
        }
        
        import BoxOffice._

        actorRef ! CreateEvent("RHCP", 20000)

        expectMsg(EventCreated(Event("RHCP", 20000)))

        actorRef ! GetTickets("RHCP", 1)

        expectMsg(Tickets("RHCP", Vector(Ticket(1))))
      }


      enterBarrier("finished")
    }
  }
}
