

object PatternMatchFun {
  def fibonacci(in: Int): Int = in match {
    case n if n <= 0 => 0
    case 1 | 2 => 1
    case n => fibonacci(n - 1) + fibonacci(n - 2)
  }

  def myMules(name: String) = name match {
    case "Elwood" | "Madeline" => Some("Cat")
    case "Archer" => Some("Dog")
    case "Pumpkin" | "Firetruck" => Some("Fish")
    case _ => None
  }

  def test1(in: Any): String = in match {
    case 1 => "One"
    case "David" | "Archer" | Some("Dog") => "Walk"
    case _ => "No Clue"
  }

  def test2(in: Any) = in match {
    case s: String => "String, length " + s.length
    case i: Int if i > 0 => "Natural Int"
    case i: Int => "Another Int"
    case a: AnyRef => a.getClass.getName
    case _ => "null"
  }
  def main(argv: Array[String]) {

    println(fibonacci(6)) //f(6)=2, f(4)=3, f(5)=5, f(6)=8
    println(fibonacci(-3))

    println(myMules("Archer").getOrElse("Not Found"))
    println(myMules("test").getOrElse("Not Found"))

    println(test1(1))
    println(test1(Some("Dog")))

    println(test2(-1))
    println(test2(Some("Option"))) //scala.Some
    println(test2(null))

    /*
     * Case Class
     * They are classes that get toString, hashCode, and equals methods automatically. 
     * It turns out that they also get properties and extractors.
     * Case classes also have properties and can be constructed without using new
     * 
     * By default, the properties are read-only, and the case class is immutable
     */
    case class Person(name: String, age: Int, valid: Boolean)

    val p = Person("David", 45, true)
    println(p.name)
    println(p.toString)

    //p.name = "Fred" not allowed

    //You can make case class properties mutable with var
    case class MPerson(var name: String, var age: Int)
    val mp = MPerson("Jorge", 24)
    mp.age = 25
    println(mp)

    /*
     * Pattern matching against case class, extract data
     */
    def older(p: Person): Option[String] = p match {
      case Person(name, age, true) if age > 35 => Some(name) //it extract name, age and match valid
      case _ => None
    }

    println(older(p)) //Some(David)
    println(older(Person("David", 45, false))) //None

    /*
     * Pattern Matching in Lists
     */
    val x = 1
    val rest = List(2, 3, 4)
    println(x :: rest) //List(1,2,3,4)
    (x :: rest) match { // note the symmetry between creation and matching
      case xprime :: restprime =>
        println(xprime); println(restprime) //1 , List(2,3,4)
      case _ => println("None")
    }

    def sumOdd(in: List[Int]): Int = in match {
      case Nil => 0
      case x :: rest if x % 2 == 1 => x + sumOdd(rest)
      case _ :: rest => sumOdd(rest)
    }

    val list = List(3, 2, 4, 5)
    println(sumOdd(list)) //8

    /*
     * replace any number of contiguous identical items with just one instance
     */

    def noPairs[T](in: List[T]): List[T] = in match {
      case Nil => Nil
      case a :: b :: rest if a == b => noPairs(a :: rest)
      // the first two elements in the list are the same, so we'll
      // call noPairs with a List that excludes the duplicate element
      case a :: rest => a :: noPairs(rest)
      // return a List of the first element followed by noPairs
      // run on the rest of the List
    }

    println(noPairs(List(1, 2, 3, 3, 3, 4, 1, 1)))

    /*
     * we discard the element just preceding the "ignore" String.
     */
    def ignore(in: List[String]): List[String] = in match {
      case Nil => Nil
      case _ :: "ignore" :: rest => ignore(rest)
      // If the second element in the List is "ignore" then return the ignore
      // method run on the balance of the List
      case x :: rest => x :: ignore(rest)
      // return a List created with the first element of the List plus the
      // value of applying the ignore method to the rest of the List
    }

    println(ignore(List("a", "ignore", "ignore", "b", "c", "ignore", "d"))) //List(ignore, b,d)

    /*
     * Pattern Matching As Functions
     * you can also pass pattern matching as a parameter to other methods
     * 
     * patterns are functions and functions are instances, patterns are instances. 
     * In addition to passing them as parameters, they can also be stored for later use
     * pattern is PartialFunction[A,B] function that extends Function1[A,B] (f(A) return B)
     * 
     * PartialFunction has an isDefinedAt method 
     * so that you can test to see whether a pattern matches a given value
     */

    val list1 = List(1, "String")
    var strList = list1.filter(a => a match {
      case s: String => true
      case _ => false
    })

    println(strList)

    strList = list1.filter {
      case s: String => true
      case _ => false
    }
    println(strList)

    /*
     * handleRequest(List[String])(PartialFunction[List[String], String) return String
     * 
     * exceptions or PartialFunction is match pattern return string
     */
    def handleRequest(req: List[String])(
      exceptions: PartialFunction[List[String], String]): String =
      //if pattern find match, apply pattern else return "Handling URL..."  
      if (exceptions.isDefinedAt(req)) exceptions(req) else
        "Handling URL " + req + " in the normal way"

    def doApi(call: String, params: List[String]): String =
      "Doing API call " + call

    var handleResponse = handleRequest("foo" :: Nil) {
      case "api" :: call :: params => doApi(call, params)
    }
    println(handleResponse) //"Handling URL List(foo) in the normal way"

    handleResponse = handleRequest(List("api", "http://scala", "param1", "param2")) {
      case "api" :: call :: params => doApi(call, params)
    }
    println(handleResponse) //Doing API call http://scala

    /*
     * Partial functions can be composed into a single function using the orElse method
     */

    val f1: PartialFunction[List[String], String] = {
      case "stuff" :: Nil => "Got some stuff"
    }
    val f2: PartialFunction[List[String], String] = {
      case "other" :: params => "Other: " + params
    }
    
    val f3 = f1 orElse f2

    handleResponse = handleRequest("other" :: "param1" :: "param2" :: Nil)(f3)
    println(handleResponse) //Other: List(param1, param2)
  }
}