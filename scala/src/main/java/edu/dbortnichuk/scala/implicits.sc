trait CanFoo[A] {
  def foos(x: A): String
}

object CanFoo {
  def apply[A:CanFoo]: CanFoo[A] = implicitly
}

case class Wrapper(wrapped: String)

implicit object WrapperCanFoo extends CanFoo[Wrapper] {
  def foos(x: Wrapper) = x.wrapped
}

def foo[A:CanFoo](thing: A) = CanFoo[A].foos(thing)

foo(Wrapper("hi"))

//----


trait CanBeEaten[A] {
  def eatMe(food: A): String
}

object CanBeEaten {
  def apply[A: CanBeEaten]: CanBeEaten[A] = implicitly
}

case class Salt(color: String, taste: String)
case class Pepper(form: String)
//case class Hooch(state: String)

implicit object SaltCanBeEaten extends CanBeEaten[Salt] {
  def eatMe(food: Salt) = "Salt: " + food.color + " " + food.taste
}

implicit object PepperCanBeEaten extends CanBeEaten[Pepper] {
  def eatMe(food: Pepper) = "Pepper: " + food.form
}

//def eat[A](food: A)(implicit evidence: CanBeEaten[A]) = "eating: " + evidence.eatMe(food)
//def eat[A: CanBeEaten](food: A) = "eating: " + implicitly[CanBeEaten[A]].eatMe(food)
def eat[A: CanBeEaten](food: A) = "eating: " + CanBeEaten[A].eatMe(food)

eat(Salt("white", "salty"))
eat(Pepper("round"))
//eat(Hooch("hooch"))

