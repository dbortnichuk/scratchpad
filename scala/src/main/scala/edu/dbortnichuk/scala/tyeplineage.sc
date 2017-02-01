trait A{
  val state = "A"
  def display() =  "From A.display"
}

trait B extends A{
  override val state = "B"
  override def display() = "From B.display"
}

trait C extends A{
  override val state = "C"
  override def display() = "From C.display"
}

trait E extends A{
  override val state = "E"
  override def display() = "From E.display"
}

class D extends B with C with E{ }

val d = new D
d state