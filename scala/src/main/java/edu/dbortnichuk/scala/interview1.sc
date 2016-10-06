import scala.util.Try

case class Person(var name: String, age: Int)
val person1 = Person("Jean", 2)

person1.name = "Dolph"

def validateName(name: String): Option[String] = {
  if (name.isEmpty) None
  else Some(name)
}

def validateAge(age: Int): Option[Int] = {
  if (age < 18) None
  else Some(age)
}

val nums = Seq(1, 20)
val names = Seq("Silvester", "Westley")

validateName("").getOrElse("noname")

for {
//  name <- validateName("Arnold")
//  age <- validateAge(19)
//  age <- nums if age > 10
//  name <- names if name.length > 7
  age <- nums
  name <- validateName("Jean")
} yield {
  Person(name, age)
}

def validateName1(name: String): Either[String, String] = {
  if (name.isEmpty) Left("Name cannot be empty")
  else Right(name)
}

for {
  age <- nums
  name <- validateName1("Jean").right.toOption
} yield {
  Person(name, age)
}

def parseInt(value: String): Try[Int] = Try(value.toInt)

val eithers = List(Left("bad"), Right("Arnold"), Right("Bruce"))

def filterMe[U,T](in: List[Either[U,T]]): List[T] = in.collect{
  case Right(r) => r
}

eithers.collect{case Right(r) => r}

