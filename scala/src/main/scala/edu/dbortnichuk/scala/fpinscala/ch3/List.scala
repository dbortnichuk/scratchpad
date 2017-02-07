package edu.dbortnichuk.scala.fpinscala.ch3

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader

import scala.runtime.Nothing$

/**
  * Created by d.bortnichuk on 2/2/17.
  */
sealed trait List[+A]

// `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing]

// A `List` data constructor representing the empty list
/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object Main extends App {

  //  println("sum: " + List.sum(List(1, 2)))
  //  println("append: " + List.append(List(1, 2), List(3, 4)))
  //  println("tail: " + List.tail(List(1, 2, 3, 4)))
  //  println("drop: " + List.drop(List(1, 2, 3, 4), 2))
  //  println("dropWhile: " + List.dropWhile(List(1, 2, 3, 4), (x: Int) => x < 4))
  //  println("init: " + List.init(List(1, 2, 3, 4)))
  //println("product: " + List.product2(List(2, 2, 2)))

  //println("foldRightNilCons: " + List.foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_)))
  //println("length: " + List.length(List(1, 2, 3, 4)))
  //println("foldLeft: " + List.foldLeft(List(1, 2, 3, 4), 0)(_ + _))
  //println("reverse: " + List.reverse(List(1, 2, 3, 4)))
   //println("append1: " + List.append1(List(1, 2, 3, 4), List(3, 4, 5, 6)))
  println("concat: " + List.concat(List(List(1, 2, 3, 4), List(1, 2, 3, 4), List(1, 2, 3, 4), List(1, 2, 3, 4))))
  print(List.counter)
}

object List {

  var counter = 0
  // `List` companion object. Contains functions for creating and working with lists.
  def sum(ints: List[Int]): Int = ints match {
    // A function that uses pattern matching to add up a list of integers
    case Nil => 0 // The sum of the empty list is 0.
    case Cons(x, xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))
    }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x, y) => x + y)

  def product2(ns: List[Double]) = {
    foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar
  }

  def sum3(ns: List[Int]) =
    foldLeft(ns, 0)(_ + _)

  def product3(ns: List[Double]) = {
    foldLeft(ns, 1.0)(_ * _)
  }

  def tail[A](l: List[A]): List[A] = drop(l, 1)

    def head[A](l: List[A]): A = {
      l match {
        case Nil => throw new NoSuchElementException("head of empty list")
        case Cons(x, xs) => x
      }
    }

  def setHead[A](l: List[A], h: A): List[A] =
    l match {
      case Nil => List(h)
      case Cons(x, xs) => Cons(h, xs)
    }

  def drop[A](l: List[A], n: Int): List[A] = {
    @annotation.tailrec
    def loop(l: List[A], idx: Int): List[A] = {
      l match {
        case Nil => Nil
        case Cons(x, xs) =>
          if (idx <= 0) l
          else loop(xs, idx - 1)
      }
    }

    loop(l, n)
  }

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    @annotation.tailrec
    def loop(l: List[A]): List[A] = {
      l match {
        case Nil => Nil
        case Cons(x, xs) =>
          if (!f(x)) l
          else loop(xs)
      }
    }

    loop(l)
  }

  def init[A](l: List[A]): List[A] = {
    l match {
      case Cons(x, Nil) => List()
      case Cons(x, xs) => List.append(List(x), init(xs))
    }
  }

  def length[A](l: List[A]): Int = {
    foldRight(l, 0)((el: A, length: Int) => length + 1)
  }

  def length2[A](l: List[A]): Int = {
    foldLeft(l, 0)((length: Int, el: A) => length + 1)
  }

  @annotation.tailrec
  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = {
    counter += 1
    l match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }
  }

  def reverse[A](l: List[A]): List[A] = {
    foldLeft(l, Nil:List[A])((nl, el) => Cons(el, nl))
  }

  def foldRight1[A, B](as: List[A], z: B)(f: (A, B) => B): B = ??? //in terms of foldLeft

  def append1[A](a1: List[A], a2: List[A]): List[A] ={
    foldLeft(reverse(a1), a2)((nl, el) => Cons(el, nl)) // bad performance because of reversing
  }


  def concat[A](ll: List[List[A]]): List[A] = {
    @annotation.tailrec
    def loop(lofl: List[List[A]], acc: List[A]): List[A] ={
      lofl match {
        case Nil => acc
        case Cons(xs, xss) => loop(xss, List.append1(acc, xs))
      }
    }

    loop(ll, Nil)
  }


  def map[A, B](l: List[A])(f: A => B): List[B] = ???
}
