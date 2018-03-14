
trait OCarVisitor {
  def visit(wheel: OWheel): Unit
  def visit(engine: OEngine): Unit
  def visit(body: OBody): Unit
  //def visit(car: OCar): Unit
}

trait OCarElement {
  def accept(visitor: OCarVisitor): Unit
}

class OWheel(val name: String) extends OCarElement {
  def accept(visitor: OCarVisitor) {
    visitor.visit(this) //visit wheel
  }
}

class OEngine extends OCarElement {
  def accept(visitor: OCarVisitor) = visitor.visit(this) //visit engine
}

class OBody extends OCarElement {
  def accept(visitor: OCarVisitor) = visitor.visit(this) //visit body
}

class OCar extends OCarElement {
  val elements = List(new OEngine, new OBody, new OWheel("FR"), new OWheel("FL"),
    new OWheel("RR"), new OWheel("RL"))

  /*
   * call each element.accept, so OCarVisitor will call all of visit methods
   */
  def accept(visitor: OCarVisitor) = (elements).foreach(_.accept(visitor))     
}

class OCarVisitorClass extends OCarVisitor {
  def visit(wheel: OWheel): Unit = {
    println("Wheel " + wheel.name)
  }
  def visit(engine: OEngine): Unit = {
    println("Engine")
  }
  def visit(body: OBody): Unit = {
    println("Body")
  }
}

object OVisitorPatternDemo {
  def main(argv: Array[String]) {
    
    val car = new OCar
    car.accept(new OCarVisitorClass)
    println("End")
  }
}