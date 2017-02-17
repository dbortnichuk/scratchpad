//package edu.dbortnichuk.scala.fpinscala.ch4

import scala.{Either => _, Left => _, Option => _, Right => _, _}
import edu.dbortnichuk.scala.fpinscala.ch4._
/**
  * Not implemented
  * Created by d.bortnichuk on 2/17/17.
  */
case class Person(name: Name, age: Age)
sealed class Name(val value: String)
sealed class Age(val value: Int)

object Person {

  def mkName(name: String): Either[String, Name] =
    if (name == "" || name == null) Left("Name is empty.") else Right(new Name(name))

  def mkAge(age: Int): Either[String, Age] = if (age < 0) Left("Age is out of range.") else Right(new Age(age))

  def mkPerson(name: String, age: Int): Either[String, Person] = mkName(name).map2(mkAge(age))(Person(_, _))

}

object MainPerson extends App {

  println(Person.mkPerson("", -1))

}
