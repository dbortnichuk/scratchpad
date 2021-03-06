package edu.dbortnichuk.akka.aia.ch14

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import edu.dbortnichuk.akka.aia.ch14.JobReceptionist.JobRequest

object Main extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("words", config)

  println(s"Starting node with roles: ${Cluster(system).selfRoles}")

  if(system.settings.config.getStringList("akka.cluster.roles").contains("master")) {
    Cluster(system).registerOnMemberUp {
      val receptionist = system.actorOf(Props[JobReceptionist], "receptionist")
      println("Master node is ready.")

      val text = List("this is a test", "of some very naive word counting", "but what can you say", "it is what it is")
      receptionist ! JobRequest("the first job", (1 to 100000).flatMap(i => text ++ text).toList)
      system.actorOf(Props(new ClusterDomainEventListener), "cluster-listener")
    }
  }
}