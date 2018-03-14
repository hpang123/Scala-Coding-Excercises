
trait CarElement
case class Wheel(name: String) extends CarElement
case class Engine() extends CarElement
case class Body() extends CarElement
case class Car(elements: List[CarElement]) extends CarElement

object VisitorPatternDemo {
  def doSomething(in: CarElement): Unit = in match {
    case Wheel(name) => println("Wheel " + name)
    case Engine() => println("Engine")
    case Body() => println("Body")
    case Car(e) => e.foreach(doSomething) //same as e.foreach(doSomething(_))
  }

  def main(argv: Array[String]) {
    var carElements = List(new Engine, new Body, new Wheel("FR"), new Wheel("FL"),
      new Wheel("RR"), new Wheel("RL"))

    var car = new Car(carElements)
    doSomething(car)

  }
}