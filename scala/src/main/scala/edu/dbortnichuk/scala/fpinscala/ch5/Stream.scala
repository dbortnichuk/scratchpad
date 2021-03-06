package edu.dbortnichuk.scala.fpinscala.ch5

/**
  * Created by d.bortnichuk on 2/17/17.
  */
import Stream._
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader

import scala.annotation.tailrec
trait Stream[+A] {

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }

  def toList: List[A] = {
    this match {
      case Empty => Nil
      case Cons(h, t) => h() :: t().toList
    }
  }

  def take(n: Int): Stream[A] = {
    this match {
      case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
      case Cons(h, _) if n == 1 => cons(h(), empty)
      case _ => empty
    }
  }


  def drop(n: Int): Stream[A] = {
    this match {
      case Cons(h, t) if n > 1 => t().drop(n - 1)
      case Cons(_, t) if n == 1 => t()
      case _ => empty
     }
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    this match {
      case Cons(h, t) if p(h()) => cons(h(), t().takeWhile(p))
      case _ => empty
    }
  }

  def forAll(p: A => Boolean): Boolean = {
    this match {
      case Cons(h, t) if p(h()) => t().forAll(p)
      case Cons(h, _) if !p(h()) => false
      case _ => true
    }
  }

  def forAll1(p: A => Boolean): Boolean = {
    foldRight[Boolean](true)((h, res) => p(h) && res)
  }

  def takeWhile1(p: A => Boolean): Stream[A] = {
    foldRight[Stream[A]](empty)((h, rs) => if(p(h)) cons(h, rs) else empty)
  }

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def headOption1: Option[A] = foldRight[Option[A]](None)((a, _) => Some(a))

  // 5.7 map, filter, append, flatmap using foldRight. Part of the exercise is
  // writing your own function signatures.

  def startsWith[B](s: Stream[B]): Boolean = ???
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)
  def from(n: Int): Stream[Int] = ???

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = ???
}

object MainStream extends App {

  val s1 = Stream(1, 2, 3, 4)

  println("toList: " + s1.toList)
  println("take: " + s1.take(2).toList)
  println("drop: " + s1.drop(2).toList)
  println("takeWhile: " + s1.takeWhile(_ < 3).toList)
  println("takeWhile1: " + s1.takeWhile1(_ < 3).toList)
  println("forAll: " + s1.forAll(_ < 5))
  println("forAll1: " + s1.forAll1(_ < 5))
  println("headOption: " + s1.headOption)
  println("headOption1: " + s1.headOption1)
}
