import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

def randomInteger() = 4
val future1 = Future(randomInteger())
val future2 = Future(randomInteger())

def add(f1: Future[Int], f2: Future[Int]): Future[Int] =
  for{result1 <- f1
      result2 <- f2}
    yield result1 + result2

val future3 = add(future1, future2)


val future4 = add(Future(throw new IllegalArgumentException), Future(randomInteger()))

val future1a = Future(throw new IllegalArgumentException)

val future1b = future1a recover { case e: IllegalArgumentException => randomInteger() }

val future3a = add(future1b, future2)

future3a.onComplete{a =>
  print(a)
}