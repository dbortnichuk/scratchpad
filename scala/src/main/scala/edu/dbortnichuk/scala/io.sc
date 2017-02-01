import scala.io.Source
import scala.util.{Success, Try}

val source = Try {
  Source.fromFile("D:\\samples\\movies\\data\\ratings1.csv")
}
val contents = source.flatMap { src =>
  Try {
    src.getLines().toList
  }
}

val conts = contents match {
  case Success(l) => print(l)
}


contents.failed.foreach(t => throw t)