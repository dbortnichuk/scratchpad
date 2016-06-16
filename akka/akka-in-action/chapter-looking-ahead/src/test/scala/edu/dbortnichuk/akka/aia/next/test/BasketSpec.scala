package edu.dbortnichuk.akka.aia.next.test

import akka.actor._
import edu.dbortnichuk.akka.aia.next.{TypedBasket, Items, Basket, Item}

class BasketSpec extends PersistenceSpec(ActorSystem("test"))
    with PersistenceCleanup {

  val shopperId = 2L
  val macbookPro = Item("Apple Macbook Pro", 1, BigDecimal(2499.99))
  val displays = Item("4K Display", 3, BigDecimal(2499.99))

  "The basket" should {
    "return the items" in {
      val basket = system.actorOf(Basket.props, Basket.name(shopperId))
      basket ! Basket.Add(macbookPro, shopperId)
      basket ! Basket.Add(displays, shopperId)

      basket ! Basket.GetItems(shopperId)   //<co id="next_works_"/>
      //basket ! Basket.GetItems            //<co id="next_does_not_work_no_error"/>
      expectMsg(Items(macbookPro, displays))
      killActors(basket)
    }
//<start id="typesafe_test"/>
    "return the items in a typesafe way" in {
      import akka.typed.AskPattern._
      import akka.typed._

      import scala.concurrent.{Await, Future}
      import scala.concurrent.duration._

      implicit val timeout = akka.util.Timeout(1 second)

      val macbookPro =
        TypedBasket.Item("Apple Macbook Pro", 1, BigDecimal(2499.99))
      val displays =
        TypedBasket.Item("4K Display", 3, BigDecimal(2499.99))

      val sys: ActorSystem[TypedBasket.Command] =
        ActorSystem("typed-basket", Props(TypedBasket.basketBehavior))

      sys ! TypedBasket.Add(macbookPro, shopperId)
      sys ! TypedBasket.Add(displays, shopperId)

      val items: Future[TypedBasket.Items] =
        sys ? (TypedBasket.GetItems(shopperId, _))

      val res = Await.result(items, 10 seconds)
      res should equal(TypedBasket.Items(Vector(macbookPro, displays)))
      //sys ? Basket.GetItems            //<co id="does_not_compile"/>
      sys.terminate()
    }
//<end id="typesafe_test"/>
  }
}
