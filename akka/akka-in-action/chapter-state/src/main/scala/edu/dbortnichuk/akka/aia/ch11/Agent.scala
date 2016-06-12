package edu.dbortnichuk.akka.aia.ch11

import akka.actor.ActorSystem
import akka.agent.Agent

import scala.concurrent.Await
import scala.concurrent.duration._
//import concurrent.ExecutionContext.Implicits.global

//<start id="ch10-agent-state"/>
case class BookStatistics(val nameBook: String, nrSold: Int)
case class StateBookStatistics(val sequence: Long,
                            books: Map[String, BookStatistics])
//<end id="ch10-agent-state"/>

class BookStatisticsMgr(system: ActorSystem) {
  implicit val ex = system.dispatcher //todo: change chapter 2.2 =>2.3
  val stateAgent = Agent(new StateBookStatistics(0, Map())) //todo: change chapter 2.2 =>2.3

  def addBooksSold(book: String, nrSold: Int) {
    stateAgent send (oldState => {
      val bookStat = oldState.books.get(book) match {
        case Some(bookState) =>
          bookState.copy(nrSold = bookState.nrSold + nrSold)
        case None => new BookStatistics(book, nrSold)
      }
      oldState.copy(oldState.sequence + 1,
        oldState.books + (book -> bookStat))
    })
  }

  def addBooksSoldAndReturnNewState(book: String,
                                    nrSold: Int): StateBookStatistics = {
    val future = stateAgent alter (oldState => {
      val bookStat = oldState.books.get(book) match {
        case Some(bookState) =>
          bookState.copy(nrSold = bookState.nrSold + nrSold)
        case None => new BookStatistics(book, nrSold)
      }
      oldState.copy(oldState.sequence + 1,
        oldState.books + (book -> bookStat))
    })
    Await.result(future, 1 second)
  }

  def getStateBookStatistics(): StateBookStatistics = {
    stateAgent.get()
  }
}
