package edu.dbortnichuk.scala


import language.experimental.macros

import reflect.macros.Context
/**
  * Created by dbort on 06.10.2016.
  */
object DebugMacros {

  def hello(): Unit = macro hello_impl

  def hello_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }

}
