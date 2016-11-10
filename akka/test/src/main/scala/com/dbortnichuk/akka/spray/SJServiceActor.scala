package com.dbortnichuk.akka.spray


import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport._
import MyJsonProtocol._


/**
  * Created by dbort on 01.11.2016.
  */
class SJServiceActor extends Actor with HttpService{

  // required as implicit value for the HttpService
  // included from SJService
  def actorRefFactory = context

  // we don't create a receive function ourselve, but use
  // the runRoute function from the HttpService to create
  // one for us, based on the supplied routes.
  def receive = runRoute(aSimpleRoute ~ anotherRoute)


  val aSimpleRoute = {}

  val anotherRoute = {}

}

import spray.json.DefaultJsonProtocol

object MyJsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat = jsonFormat3(Person)

}

case class Person(name: String, fistName: String, age: Long)



