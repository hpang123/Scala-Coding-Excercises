
/*
 * Factory Method
 */
trait Vehicle1
private class Car1 extends Vehicle1
private class Bike1 extends Vehicle1

object Vehicle1 {
  def apply(kind: String) = kind match {
    case "car" => new Car1()
    case "bike" => new Bike1()
  }
}

object DesignPattern {
  def main(args: Array[String]): Unit = {

    Vehicle1("car")

    /*
 		*  Strategy Pattern
 		*/
    type Strategy = (Int, Int) => Int
    class Context(operation: Strategy) {
      def execute(a: Int, b: Int) = { operation(a, b) }
    }
    val add: Strategy = _ + _
    val multiply: Strategy = _ * _

    val result = new Context(multiply).execute(5, 5)
    println(result)

    /* Adapter
     * 
     */
    trait ServiceProviderInterface {
      def service(message: String)
    }
    final class ServiceProviderImplementation {
      def service(kind: String, property: String) { println(kind + " " + property) }
    }
    implicit class Adapter(impl: ServiceProviderImplementation) extends ServiceProviderInterface {
      def service(property: String) { impl.service("TYPEA", property) }
    }
    val service: ServiceProviderInterface = new ServiceProviderImplementation
    service.service("my property") //implicit convert ServiceProviderImplementation to Adapter

    /*
     * Template Method
     */
    val subMethodA = () => { println("MethodA") }
    val subMethodB = () => { println("MethodB") }

    def process(methodA: () => Unit, methodB: () => Unit) = {
      methodA()
      methodB()
    }

    process(subMethodA, subMethodB)
    process(()=>{println("A")}, ()=>{println("B")})

  }
}