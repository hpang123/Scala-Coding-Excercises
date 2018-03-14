import java.lang._
/* Tradition OOP */
trait OShape {
  def area: Double
}

class OCircle(radius: Double) extends OShape {
  def area = radius * radius * Math.PI
}

class OSquare(length: Double) extends OShape {
  def area = length * length
}

class ORectangle(h: Double, w: Double) extends OShape {
  def area = h * w
}

/*pattern match implementation  case class*/
trait Shape
case class Circle(radius: Double) extends Shape
case class Square(length: Double) extends Shape
case class Rectangle(h: Double, w: Double) extends Shape

object Shape {
  def area(shape: Shape): Double = shape match {
    case Circle(r) => r * r * Math.PI
    case Square(l) => l * l
    case Rectangle(h, w) => h * w
  }

  def perimeter(shape: Shape) = shape match {
    case Circle(r) => 2 * Math.PI * r
    case Square(l) => 4 * l
    case Rectangle(h, w) => h * 2 + w * 2
  }
}
object ShapeDemo {
  def main(argv: Array[String]) {
    println(Shape.area(new Circle(1)))
    println(Shape.area(new Rectangle(1,2)))
    
  }
}