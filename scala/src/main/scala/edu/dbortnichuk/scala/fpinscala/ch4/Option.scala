package edu.dbortnichuk.scala.fpinscala.ch4

import scala.util.Try

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B] = {
    this match {
      case None => None
      case Some(x) => Some(f(x))
    }
  }

  def getOrElse[B >: A](default: => B): B = {
    this match {
      case None => default
      case Some(x) => x
    }
  }

  def flatMap[B](f: A => Option[B]): Option[B] = {
    this.map(f).getOrElse(None)
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] =
    this.map(Some(_)).getOrElse(None)

  def filter(f: A => Boolean): Option[A] = {
    this.flatMap((a: A) => if (f(a)) Some(a) else None)
  }
}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]

object MainList extends App {
  val opt = Some("payload")
  val none = None

  println("map: " + opt.map(s => s.charAt(0)))
  println("getOrElse: " + none.getOrElse("default"))
  println("flatMap: " + opt.flatMap(s => Some(s + "!")))
  println("orElse: " + none.orElse(Some("default")))
  println("filter: " + opt.filter(_.startsWith("p")))
  println("map2: " + Option.map2(Some(1), Some(2))((a: Int, b: Int) => a + b))
  println("sequence: " + Option.sequence(List(Some("aa"), Some("b"))))
  println("traverse: " + Option.traverse(List("1", "2"))(i => Some(i.toInt)))
  println("sequence2: " + Option.sequence2(List(Some("aa"), Some("b"))))
}

object Option {
  def failingFn(i: Int): Int = {
    val y: Int = throw new Exception("fail!") // `val y: Int = ...` declares `y` as having type `Int`, and sets it equal to the right hand side of the `=`.
    try {
      val x = 42 + 5
      x + y
    }
    catch {
      case e: Exception => 43
    } // A `catch` block is just a pattern matching block like the ones we've seen. `case e: Exception` is a pattern that matches any `Exception`, and it binds this value to the identifier `e`. The match returns the value 43.
  }

  def failingFn2(i: Int): Int = {
    try {
      val x = 42 + 5
      x + ((throw new Exception("fail!")): Int) // A thrown Exception can be given any type; here we're annotating it with the type `Int`
    }
    catch {
      case e: Exception => 43
    }
  }

  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs).flatMap(m => mean(xs.map(x => math.pow(x - m, 2)))) //!
  }

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    for {
      aval <- a
      bval <- b
    } yield f(aval, bval)
  }

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    a.foldRight[Option[List[A]]](Some(Nil))((ea, en) => map2(ea, en)(_ :: _))

  }

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    a.foldRight[Option[List[B]]](Some(Nil))((ea, en) => map2(f(ea), en)(_ :: _))
  }

  def sequence2[A](a: List[Option[A]]): Option[List[A]] = {
     traverse(a)(x => x.flatMap(Some(_)))
  }

}
