package edu.dbortnichuk.akka.aia.ch14.mjvm

import akka.remote.testkit.MultiNodeSpecCallbacks
import org.scalatest.{MustMatchers, _}

trait STMultiNodeSpec extends MultiNodeSpecCallbacks
with WordSpecLike with MustMatchers with BeforeAndAfterAll {

  override def beforeAll() = multiNodeSpecBeforeAll()

  override def afterAll() = multiNodeSpecAfterAll()
}
