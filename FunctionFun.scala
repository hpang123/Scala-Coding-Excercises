import java.io._

object FunctionFun {
  def main(args: Array[String]): Unit = {

    val f: Int => String = x => "Dude: " + x //f(int) :String ={}
    println(f) //<Function1>

    def f1(x: Int): String = "Dude: " + x

    println(f(1) + "," + f1(1))
    println(f == f) //true

    def w42(f: Int => String) = f(42) //more like f wrapper
    println(w42(f))

    def fm(i: Int) = "fm " + i
    println(w42(fm)) //fm(42)

    println(w42((i: Int) => fm(i))) //fm(42)
    println(w42(i => fm(i))) //same as above

    println(w42(fm _)) //partially apply

    //Just like pass an anonymous inner class, 1 param Int, return string
    w42(new Function1[Int, String] {
      def apply(i: Int) = fm(i)
    })

    /*
     * pass functions that are multiline blocks of code.
     * i will be 42
     */
    var w42Val = w42 {
      i =>
        val range = 1 to i
        range.mkString(",")
    }

    println(w42Val)

    /*
     * Methods and functions are different things. In Scala, everything except a method 
     * is an instance; therefore methods are not instances. 
     * Methods are attached to instances
     */

    def plus(a: Int, b: Int) = "Result is: " + (a + b) //attach a+b results
    println(plus(1, 3))

    //Partial Application and Functions
    val p = (b: Int) => plus(42, b)
    println(p(1))

    //can define this too
    def p1(b: Int) = plus(42, b)
    println(p1(1))

    //Another way to define
    val p2: Int => String = b => plus(42, b)
    println(p2(2))
    
    val p3 = plus(42, _: Int)
    println(p3(3))

    /*
     * Parameters can be specified in different parenthesis groups
     * Curried Function
     */
    def add(a: Int)(b: Int) = "Result is: " + (a + b)
    println(add(1)(2))

    var addValue = add(1) {
      val r = new java.util.Random
      r.nextInt(100)
    }

    println(addValue)

    //powerful
    println(w42(add(1))) //add(1)(42)

    //create a function by partially apply
    def f2 = add(1) _
    println(w42(f2))

    //Type Parameter
    def t42[T](f1: Int => T): T = f1(42)

    println(t42(f))
    println(t42(1 +))
    println(t42[Int](1 +)) //explicitly define type parameter

    //def intList(i: Int) =  (1 to i).toList
    //val intList = (i: Int)=> (1 to i).toList
    val intList: Int => List[Int] = i => (1 to i).toList //function take Int param and return List[Int]
    println(t42(intList))

    /*
     * Functions are bound to the variables in the scope in which the function is created.
     * Functions can be bound to vars and vals. Functions can even modify vars
     */

    val foo = "dog"
    val whoTo = (s: String) => s + " " + foo //access foo

    println(whoTo("I love my"))

    var strs: List[String] = Nil
    val strF = (s: String) => { strs ::= s; s + " Registered" } //It will modify strs by attaching s

    println(strF("a")) //a registered
    println(strs) //List(a)

    val listValue = List("p", "q", "r").map(strF)
    println(listValue) //List(p Registered, q Registered, r Registered)
    println(strs) //List(r, q, p, a)

    /*
     * bf take Int param and return function int => Int (the function take int and return int)
     * i => v => i + v so bf(1) return f(v) = 1 + v or v => 1 + v
     */
    def bf: Int => Int => Int = i => v => i + v
    val fs = (1 to 100).map(bf).toArray
    println(fs(0)) //<Function1>
    println(fs(0)(1)) // f(1) = 1+1=2
    println(fs(44)(3)) // f(45) = 45+3=48

    def randomName = "I" + Math.abs((new java.util.Random).nextLong)

    //create a JavaScript trait interface
    trait JavaScript

    //callbacks is a Map[String, function that generate JavaScript instance
    var callbacks: Map[String, () => JavaScript] = Map()

    /*Pass function f that will return JavaScript
     * Add name as key and value f to the callbacks map
     * return <button>.. Elm
     */
    def register(f: () => JavaScript) = {
      val name = randomName
      callbacks += name -> f
      <button onclick={ "invokeSeverCall('" + name + "')" }>ClickMe</button>
    }

    /*
     * sealed trait can be extended only in the same file as its declaration
     */
    sealed trait Expr
    case class Add(left: Expr, right: Expr) extends Expr
    case class Mul(left: Expr, right: Expr) extends Expr
    case class Val(value: Int) extends Expr
    case class Var(name: String) extends Expr

    def calc(expr: Expr, vars: Map[String, Int]): Int = expr match {
      case Add(left, right) => calc(left, vars) + calc(right, vars)
      case Mul(left, right) => calc(left, vars) * calc(right, vars)
      case Val(v) => v
      case Var(name) => vars(name)
    }

    println(calc(Add(Val(1), Val(1)), Map())) //2
    println(calc(Mul(Val(3), Add(Val(1), Val(1))), Map())) //6

    val variableMap = Map("x" -> 3, "y" -> 2)
    println(calc(Mul(Val(3), Add(Var("x"), Var("y"))), variableMap)) //15

    //Call by Name
    def log(level: String, msg: => String) =
      if (level.compareToIgnoreCase("INFO") == 0)
        println(msg) //msg is a function

    val value = "my log data"
    log("INFO", "The value is " + value)

    /*
     * expr function is call inside and keep call it until it return null 
     */
    def allStrings(expr: => String): List[String] = expr match {
      case null => Nil
      case s => s :: allStrings(expr)
    }

    val br = new BufferedReader(new FileReader("foo.txt"))
    println(allStrings(br.readLine))

    /*Build Your Own Control Structures
     * Singleton Control
     * Method using [A,B] has two type parameters. using(A)(f: A=>B) return B
     * f(A) return B
     * 
     * A can be an instance of any class as long as that class has a close method on it
     */
    object Control {
      def using[A <: { def close(): Unit }, B](param: A)(f: A => B): B =
        try {
          f(param)
        } finally {
          param.close()
        }

      import scala.collection.mutable.ListBuffer
      /*
       * bmap(test)(block) return List[T]
       * 
       * test function return boolean, block function return T
       */
      def bmap[T](test: => Boolean)(block: => T): List[T] = {
        val ret = new ListBuffer[T]
        while (test) ret += block
        ret.toList
      }
    }

    Control.using(new BufferedReader(new FileReader("foo.txt"))) {
      reader => //reader is parameter passed with BufferedReader instance
        println("My Control :" + reader.readLine())
    }

    /* Example to use Control */
    case class Person(name: String, age: Int, valid: Boolean)
    
    object PersonService {
      import Control._
      import java.sql._
      def findPeople(conn: Connection): List[Person] =
        using(conn.createStatement) { st =>
          using(st.executeQuery("SELECT * FROM person")) { rs =>
            bmap(rs.next) {
              new Person(rs.getString("name"), rs.getInt("age"), rs.getBoolean("valid"))
            }
          }
        }
    }
  }

}