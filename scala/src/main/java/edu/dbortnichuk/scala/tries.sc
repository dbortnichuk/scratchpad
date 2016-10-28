import scala.util.Try

val tr1 = Try{"Hello"}
val tr2 = Try{
  throw new IllegalArgumentException
  ""
}

val tr = tr2.flatMap { s =>
  Try{
    //throw new IllegalStateException
    s + "World"
  }

}

tr.foreach(println)