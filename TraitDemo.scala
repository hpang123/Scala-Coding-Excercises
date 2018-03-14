
object TraitDemo {
  def main(argv: Array[String]) {

    /*
 		* When you add implementation to traits they become Mixins
 		* you can create a trait that inherits from a Class, as well as a Class that extends a trait
 		* It can be mixed in to a class using either the extends or with keyword
 		* Trait cannot pass parameter like class constructor
 		*/
    trait Gliding {
      def gliding() {
        println("gliding")
      }
    }

    class Glider extends Gliding {
      override def toString = "glider"
      override def gliding() {
        println("race for now " + toString)
      }
    }
    val glider: Gliding = new Glider
    glider.gliding()
    println(glider)

    trait TraitA {
      def methodA
      def methodAWithParam(param: String)
      def methodAWithReturnType: String
    }

    trait TraitB extends TraitA {
      def methodB
    }

    /*
    * class extending trait must implement all the abstract methods of trait
    * Otherwise, must be declared abstract
    */
    abstract class A extends TraitA with TraitB {

    }

    class ClassA extends TraitA {
      def methodA = println("methodA")
      def methodAWithParam(param: String) {
        println("methodAWithParam " + param)
      }
      def methodAWithReturnType: String = {
        println("methodAWithReturnType")
        "successful"
      }
    }
    /*
    * If a class extends a class and a trait, always use extends before the class name, 
    * and use with before the trait's name
    * with has to be a trait not class or abstract class
    */
    class ClassC extends ClassA with TraitA with TraitB {
      def methodB = println("methodB")
    }

    trait Vehicle {
      val seat = 4
      val color: String
      var engine: String

      def drive { println("Driving") }
      def race
    }
    class Car extends Vehicle {
      override val seat = 5 // need to override val not var
      val color = "red"
      var engine = "high power"
      def race { println("Racing the car") }
    }
    class Boat extends Vehicle {
      val color = "white"
      var engine = "middle power"
      def race = println("Racing boat")
      override def drive = println("Float")
    }

    val boat = new Boat
    boat.race
    boat.drive

    abstract class LivingThing
    abstract class Plant extends LivingThing
    abstract class Fungus extends LivingThing
    abstract class Animal extends LivingThing

    trait HasLegs extends Animal {
      def walk { println("Walking") }
    }

    trait HasWings extends Animal {
      def flap() { println("Flap Flap") }
    }

    trait Flies {
      this: HasWings => //it means when class mixed this trait, the class must to mix HasWings too.
      def fly() { println("I am flying") }
    }

    abstract class Bird extends Animal with HasWings with HasLegs

    /*when mix Flies Bird has HasWings so it is ok */
    class Robin extends Bird with Flies
    class Ostrich extends Bird //not fly

    abstract class Mammal extends Animal {
      def bodyTemperature: Double
    }

    trait KnowsName extends Animal {
      def name: String //It will like a field
    }

    //name will assign to field name so don't need to implement def name
    class Dog(val name: String) extends Mammal with HasLegs with KnowsName {
      def bodyTemperature: Double = 99.3
    }

    val dog = new Dog("hero")
    println(dog.bodyTemperature)
    println(dog.name)

    trait IgnoresName {
      this: KnowsName => //mixing IgnoresName need KnowsName
      def ignoreName(when: String): Boolean
      def currentName(when: String): Option[String] =
        if(ignoreName(when)) None else Some(name) //name is from KnowsName

    }
    
    class Cat(val name: String) extends Mammal with HasLegs with KnowsName with IgnoresName {
      def ignoreName(when: String) = when match {
        case "Dinner" => false
        case _ => true
      }
      def bodyTemperature: Double = 99.5
    }
    val cat = new Cat("Meo")
    println(cat.name)
    println(cat.currentName("Meo").getOrElse("Forget name"))
    println(cat.currentName("Dinner").getOrElse("Forget name"))
    cat.walk
    
    trait Athlete extends Animal
    
    trait Runner {
      this: Athlete with HasLegs => //Runner has to mix with Athlete and HasLegs
      def run() {println("I am running")}
    }
    
    class Person(val name: String) extends Mammal with HasLegs with KnowsName{
      def bodyTemperature: Double = 98.6 
    }
    
    trait Biker extends Person {
      this: Athlete =>
      def ride() {println("I am riding my bike")}
    }
    
    trait Gender
    trait Male extends Gender
    trait Female extends Gender
    
    //Dog extends Mammal, Runner required Athlete, Dog has legs
    val archer = new Dog("biker") with Athlete with Runner with Male
    
    val dpp = new Person("David") with Athlete with Biker with Male
    val john = new Person("John") with Athlete with Biker with Male
    val annette = new Person("Annette") with Athlete with Runner with Female
    
    def goBiking(b: Biker) = println(b.name + " is biking")
    goBiking(dpp)
    //goBiking(annette) //mismatch type
    
    //r must be Person plus Runner
    def charityRun(r: Person with Runner) = r.run
    charityRun(annette)
    //charityRun(archer) //mismatch type due to not a person
    
  }
}