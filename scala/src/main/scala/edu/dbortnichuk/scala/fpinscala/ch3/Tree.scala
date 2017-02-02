package edu.dbortnichuk.scala.fpinscala.ch3

/**
  * Created by d.bortnichuk on 2/2/17.
  */
sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


object Tree {




}
