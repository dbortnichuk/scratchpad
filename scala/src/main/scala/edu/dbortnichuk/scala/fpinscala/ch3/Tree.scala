package edu.dbortnichuk.scala.fpinscala.ch3

/**
  * Created by d.bortnichuk on 2/2/17.
  */
sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object MainTree extends App {

  val l1 = new Leaf[String]("chery")
  val l2 = new Leaf[String]("watermelon")
  val l3 = new Leaf[String]("potato")
  val l4 = new Leaf[String]("plum")
  val b1 = new Branch[String](l1, l2)
  val b2 = new Branch[String](b1, l3)
  val b3 = new Branch[String](b2, l4)

  val l11 = new Leaf[Int](9)
  val l21 = new Leaf[Int](-5)
  val b11 = new Branch[Int](l11, l21)

  //println("size: " + Tree.size[Int](b1))
  //println("maximum: " + Tree.maximum(b11))
  //println("depth: " + Tree.depth(b2))
  //println("map: " + Tree.map(b3)((x: String) => x.length))
  //println("fold: " + Tree.fold(b3, 0)((x: String) => x.length)(_ + _))
  ///println("size1: " + Tree.size1(b3))
  //println("maximum1: " + Tree.maximum1(b11))
  //println("depth1: " + Tree.depth1(b2))
}


object Tree {

  def size[A](t: Tree[A]): Int =
    t match {
      case Leaf(_) => 1
      case Branch(l, r) => size(l) + size(r) + 1
    }

  def maximum(t: Tree[Int]): Int = {
    t match {
      case Leaf(x) => x
      case Branch(l, r) => maximum(l) max maximum(r)
    }
  }

  def depth[A](t: Tree[A]): Int = {
    t match {
      case Leaf(_) => 1
      case Branch(l, r) => (depth(l) + 1) max (depth(r) + 1)
    }
  }

  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = {
    t match {
      case Leaf(x) => Leaf(f(x))
      case Branch(l, r) => Branch(map(l)(f), map(r)(f))
    }
  }

  def fold[A, B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = {
    t match {
      case Leaf(x) => f(x)
      case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
    }
  }

  def size1[A](t: Tree[A]): Int = {
      fold(t)(_ => 1)(_ + _ + 1)
  }

  def maximum1[A](t: Tree[Int]): Int = {
    fold(t)((a: Int) => a)(_ max _)
  }

  def depth1[A](t: Tree[A]): Int = {
    fold(t)(_ => 1)(1 + _ max _)
  }

  def map1[A, B](t: Tree[A])(f: A => B): Tree[B] = {
    fold(t)((v: A) => Leaf(f(v)): Tree[B])(Branch(_, _))
  }


}
