import java.io._

object ObjectDemo {
  class Shape {
    def area: Double = 0.0
  }

  /* val means the field cannot be changed */
  class Rectangle(val width: Double, var height: Double) extends Shape {
    /* 
     * Demo Auxiliary Constructor: 
     * define name as this
     * must begin with a call to a previously defined constructor
    */
    def this(width: Double) {
      this(width, 1)
    }

    def this() {
      this(2)
      this.height = 4
    }

    override def area = width * height
  }

  //you can give a default value
  class Circle(val radius: Double = 1) extends Shape {
    override def area = math.Pi * radius * radius
  }

  /*
   * prevent getter and setter from generated, 
   * only accessed within the class
   */
  class Book(private var title: String) {
    /*body will run as the constructor */
    println("initial to create Book object")
    def printTitle { println(title) }
    def printTitle(b: Book) {
      println(b.title) //you can access title in this class
    }
  }

  /*
   * If no val or var in constructor, no getter/setter
   * you cannot access b.title like Book(private var title: String)
   */
  class Book1(title: String) {
    def printTitle(b: Book) {
      //println(b.title) //you cannot access title 
    }
  }

  class Vehicle(speed: Int) {
    val mph: Int = speed
    def race() = println("Racing")
  }

  class Car(speed: Int) extends Vehicle(speed) {
    //val mph: Int = speed
    override def race() = println("Racing Car")
  }

  trait flying {
    def fly() = println("flying")
  }
  trait gliding {
    def glid() = println("gliding")
  }

  class Batmobile(speed: Int) extends Vehicle(speed) with flying with gliding {
    override def race() = println("Racing Batmobile")
    override def fly() = println("Flying Batmobile")
  }

  /* Value Class - avoid allocating runtime objects.
   * the type at compile time is SomeClass, but runtime it represent Int.
   * it can defined def but no val, var or nested traits class
   */
  class SomeClass(val i: Int) extends AnyVal{
    def twice() =i*2
  }

  def main(args: Array[String]): Unit = {
    val circle = new Circle(3)
    println(circle.radius + ": " + circle.area)
    println((new Circle).area) //use defalut value

    val rectangle = new Rectangle
    println(rectangle.area)

    val book = new Book("Beginning Scala")
    book.printTitle
    book.printTitle(new Book("Beginning Erlang")) //"Beginning Erlang"

    /* Function */
    /* you can pass type of parameter */
    def list[T](p: T): List[T] = p :: Nil

    println(list(1))
    println(list("Hello"))

    /* last parameter can be variable-length argument 
      * as will be Seq[Int]
      * */
    def largest(as: Int*) = as.reduceLeft((a, b) => a max b)

    println(largest(1, 2, 3, 6, 9, 4))

    //foldLeft has initial value
    def mkString[T](as: T*): String = as.foldLeft("")(_ + _.toString)
    println(mkString(1, 2, "Hello"))

    /* put bounds on the type parameter - T is Number or subclass */
    def sum[T <: Number](as: T*): Double = as.foldLeft(0d)(_ + _.doubleValue)
    println(sum[Number](1, 4d, 5d))

    def readLines(br: BufferedReader) = {
      var ret: List[String] = Nil
      def readAll(): Unit = br.readLine match {
        case null =>
        case s => ret ::= s; readAll()
      }
      readAll()
      ret.reverse
    }

    println(readLines(new BufferedReader(new FileReader("foo.txt"))))

    abstract class Base {
      def thing: String
    }
    class One extends Base {
      /*don't have to override the abstract method */
      def thing = "Moof"
    }

    class Two extends One {
      //val can override a def in  a superclass
      override val thing = (new java.util.Date).toString
    }
    class Three extends One {
      override lazy val thing = super.thing + (new java.util.Date).toString
    }

    println((new Two).thing)
    println((new Three).thing)

    /* Singleton */
    object Car {
      def drive(s: String) { println(s + "drive car") }
    }
    val who = "I "
    Car.drive(who)

    /* Companion Objects
     * Both class (or trait) and an object can share the same name.
     * Let you create static members on a class, useful for implementing helper methods
     * and factory
     */
    trait Shape1 {
      def area: Double
    }
    object Shape1 {
      def help = println("Help ...")

      private class Circle(radius: Double) extends Shape1 {
        override val area = 3.14 * radius * radius
      }

      private class Rectangle(height: Double, length: Double) extends Shape1 {
        override val area = height * length
      }

      def apply(height: Double, length: Double): Shape1 = new Rectangle(height, length)
      def apply(radius: Double): Shape1 = new Circle(radius)
    }
    //it will call apply(Double)
    val circle1 = Shape1(2)
    println(circle1.area)
    Shape1.help

    val car = new Car(10)
    car.race()
    println(car.mph)

    val batmobile = new Batmobile(100)
    batmobile.fly
    batmobile.glid
    val vehicle = new Vehicle(100) 
    vehicle.race()

    val vehicleList = List(car, batmobile)
    val fatestVehicle = vehicleList.maxBy(_.mph)
    println(fatestVehicle.mph)

    /* Case class: automatically create toString, equal, hashCode,
     * don't need the new statement
     * all parameters in constructor become properties of class
     *
     */
    case class Stuff1(name: String, age: Int)
    val s = Stuff1("David", 45)
    val s1 = Stuff1("David", 45)
    println(s.toString())
    println(s == s1) //true
    println(s == Stuff1("David", 45)) //true

    val v = new SomeClass(9)
    println(v.twice())
    println(v)
  }
}