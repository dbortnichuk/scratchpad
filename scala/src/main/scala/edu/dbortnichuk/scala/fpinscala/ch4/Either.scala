import scala.{Either => _, Left => _, Option => _, Right => _, _}

// hide std library `Option` and `Either`, since we are writing our own in this chapter

sealed trait Either[+E, +A] {
  def map[B](f: A => B): Either[E, B] = {
    this match {
      case Left(e) => Left(e)
      case Right(x) => Right(f(x))
    }
  }

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = {
    this match {
      case Left(e) => Left(e)
      case Right(a) => f(a)
    }
  }

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = {
    this match {
      case Left(_) => b
      case Right(a) => Right(a)
    }
  }

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = {
    for {aa <- this
         bb <- b
    } yield f(aa, bb)
  }

}

case class Left[+E](get: E) extends Either[E, Nothing]

case class Right[+A](get: A) extends Either[Nothing, A]



object Either {
  def traverse[E, A, B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    es.foldRight[Either[E, List[B]]](Right(Nil))((elA, elNew) => f(elA).map2(elNew)(_ :: _))
  }

  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = {
    es.foldRight[Either[E, List[A]]](Right(Nil))((elA, elNew) => elA.map2(elNew)(_ :: _))
  }

  def sequence2[E, A](es: List[Either[E, A]]): Either[E, List[A]] = {
    traverse(es)(el => el.flatMap(Right(_)))
  }

  def mean(xs: IndexedSeq[Double]): Either[String, Double] =
    if (xs.isEmpty)
      Left("mean of empty list!")
    else
      Right(xs.sum / xs.length)

  def safeDiv(x: Int, y: Int): Either[Exception, Int] =
    try Right(x / y)
    catch {
      case e: Exception => Left(e)
    }

  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch {
      case e: Exception => Left(e)
    }

}

object MainEither extends App {
  val eitherR = Right("payload1")
  val eitherR1 = Right("payload2")
  val eitherL = Left(new IllegalArgumentException("error"))

  println("map: " + eitherR.map((x: Any) => x))
  println("flatMap: " + eitherR.flatMap(s =>
    if (s.length < 5) Right(s)
    else Left(new IllegalArgumentException("line to short")))
  )
  println("orElse: " + eitherR.orElse(Right("default")))
  println("map2: " + eitherR.map2(eitherR1)((s1, s2) => s1.length + s2.length))
  println("traverse: " + Either.traverse(List(1, 2, 3, 4))(n =>
    if (n > 0) Right(n)
    else Left(new IllegalArgumentException("negative number"))
  ))
  println("sequence: " + Either.sequence(List(Right(1), Right(2))))
  println("sequence2: " + Either.sequence2(List(Right(1), Right(2))))

}
