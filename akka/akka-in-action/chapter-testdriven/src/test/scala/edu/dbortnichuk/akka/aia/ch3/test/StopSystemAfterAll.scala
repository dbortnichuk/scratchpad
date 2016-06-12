package edu.dbortnichuk.akka.aia.ch3.test

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}
;

trait StopSystemAfterAll extends BeforeAndAfterAll {
		//<co id="ch02-stop-system-before-and-after-all"/>
  this: TestKit with Suite => //<co id="ch02-stop-system-self-type"/>
  override protected def afterAll() {
    super.afterAll()
    system.terminate() //<co id="ch02-stop-system-terminate"/>
  }
}
