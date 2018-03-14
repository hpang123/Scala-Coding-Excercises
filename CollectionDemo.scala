//have to use 2.10.x Scala lib where scala-xml is not separated yet
import scala.xml._
import scala.xml.transform._

/*
 * imports java.lang.Boolean but renames it locally to JBool 
 * so it doesn't conflict with Scala's Boolean class.
 * See we can import java package
 */
import java.lang.{ Boolean => JBool }

trait Person {
  def first: String
  def age: Int
  def valid: Boolean
}

object CollectionDemo {
  def isOdd(x: Int) = x % 2 == 1
  def isEven(x: Int) = !isOdd(x)
  def isOdd(in: Long) = in % 2L == 1L

  //pattern match
  def roman(in: List[Char]): Int = in match {
    case 'I' :: 'V' :: rest => 4 + roman(rest)
    case 'I' :: 'X' :: rest => 9 + roman(rest)
    case 'I' :: rest => 1 + roman(rest)
    case 'V' :: rest => 5 + roman(rest)
    case 'X' :: 'L' :: rest => 40 + roman(rest)
    case 'X' :: 'C' :: rest => 90 + roman(rest)
    case 'X' :: rest => 10 + roman(rest)
    case 'L' :: rest => 50 + roman(rest)
    case 'C' :: 'D' :: rest => 400 + roman(rest)
    case 'C' :: 'M' :: rest => 900 + roman(rest)
    case 'C' :: rest => 100 + roman(rest)
    case 'D' :: rest => 500 + roman(rest)
    case 'M' :: rest => 1000 + roman(rest)
    case _ => 0
  }

  def main(args: Array[String]): Unit = {
    val x = List(1, 2, 3, 4) //List implements apply[T](param: T*): List[T], so we can call list()

    val evenLst = x.filter(a => a % 2 == 0) //x doesn't change
    println(evenLst)

    val oddList = x.filter(isOdd)
    println(oddList)

    val array = Array(1, 2, 3)
    println(array(1)) // 2

    val m = Map("one" -> 1, "two" -> 2, "three" -> 3)
    println(m("three")) // 3

    /*
     * actual elements in the Range are not instantiated 
     * until they are accessed
     * range is immutable
     */
    val range = 0 to 10
    println(range)
    val range1 = (1 to Integer.MAX_VALUE - 1).take(5)
    println(range1) //Range(1, 2, 3, 4, 5)

    (1 to Integer.MAX_VALUE - 1).take(5)

    /*
     * :: operator as the first character is evaluated right to left
     * So the following is same as:
     * new ::(1, new ::(2, new ::(3, Nil)))
     * ::(head, tail)
     */
    val list1 = 1 :: 2 :: 3 :: Nil
    println(list1) //List(1,2,3)

    List[Number](1, 44.5, 8d) //Sometimes the type reference will help

    //to prepend an item to the head of list, use :: to create new one
    val newList = 99 :: x
    println(newList) //List(99, 1, 2, 3, 4)

    val y = List(99, 98, 97)
    //To merge lists
    val mergeList = x ::: y
    println(mergeList) //List(1, 2, 3, 4, 99, 98, 97)

    //toList convert to List[Char]
    val filterList = "99 Red Balloons".toList.filter(Character.isDigit)
    println(filterList) //List(9,9)

    //get chars until first space
    println("Elwood eats mice".takeWhile(c => c != ' ')) //Elwood

    //Transformation with map
    println(List("A", "Cat").map(s => s.toLowerCase)) //List(a, cat)
    //shorten the function that same as above
    List("A", "Cat").map(_.toLowerCase)
    println(List("A", "Cat").map(_.length)) //List(1, 3)

    //instaniate that anonymous class that implements methods
    val d = new Person {
      def first = "David"
      def age = 50
      def valid = true
    }
    val e = new Person {
      def first = "Elwood"
      def age = 10
      def valid = true
    }
    val a = new Person {
      def first = "Archer"
      def age = 25
      def valid = false
    }

    println(List(a, d, e).map(_.first)) //List(Archer, David, Elwood)

    println(List(a, d, e).map(n => <li>{ n.first }</li>)) //List(<li>Archer</li>, <li>David</li>, <li>Elwood</li>)

    println(List(99, 2, 1, 45).sorted) //List(1, 2, 45, 99)
    println(List(99, 2, 1, 45).sortWith(_ > _)) //List(99, 45, 2, 1)
    println(List("b", "a", "elwood", "archer").sortWith(_.length > _.length)) //List(elwood, archer, a, b)

    def validByAge(in: List[Person]) =
      in.filter(_.valid).
        sortWith(_.age < _.age).
        map(_.first)

    println(validByAge(List(a, d, e))) //List(Elwood, David)
    
    def filterValid(in: List[Person]) = in.filter(_.valid)
    def sortPeopleByAge(in: List[Person]) = in.sortWith(_.age < _.age)
    def newValidByAge(in: List[Person]) =
      (filterValid _ andThen sortPeopleByAge _ )(in).map(_.first)
      
    println(newValidByAge(List(a, d, e)))
    
    val filterSort = filterValid _ andThen sortPeopleByAge
    def newValidByAge2(in: List[Person]) = filterSort(in).map(_.first)
    
    println(newValidByAge2(List(a, d, e)))
    
    def getFirstName(in: List[Person]) = in.map(_.first)
    
    val filterSortMap = filterValid _ andThen sortPeopleByAge _ andThen getFirstName 
    //def newValidByAge3(in: List[Person]) = filterSortMap(in)
    
    //println(newValidByAge3(List(a,d,e)))
    println(filterSortMap(List(a,d,e)))
        
    /*Reduxio
     * reduceLeft will throw an exception on an Nil (empty) List.
     */
    println(List(8, 6, 22, 2).reduceLeft(_ max _)) //22
    val longWord = List("moose", "cow", "A", "Cat").
      reduceLeft((a, b) => if (a.length > b.length) a else b)

    println(longWord) //moose

    println(List(8, 6, 22, 2).reduceLeft(_ + _)) //38

    //start with the seed value
    println(List(1, 2, 3, 4).foldLeft(0)(_ + _)) //10
    println(List(1, 2, 3, 4).foldLeft(1)(_ * _)) //24

    println(List("b", "a", "elwood", "archer").foldLeft(0)(_ + _.length)) //14 total length

    val n = (1 to 3).toList
    /*nested map invocations
     * n is immutable
     * first element is (1,2,3).map(j => 1*j)
     * List[List[Int]] = List(List(1, 2, 3), List(2, 4, 6), List(3, 6, 9))
     */
    println(n.map(i => n.map(j => i * j)))

    //List(1, 2, 3, 2, 4, 6, 3, 6, 9)
    println(n.flatMap(i => n.map(j => i * j)))

    //It could get complicate
    val n10 = (1 to 10).toList
    //list of even x list of odd
    println(n10.filter(isEven).flatMap(i => n10.filter(isOdd).map(j => i * j)))

    val n10Result = for { i <- n10 if isEven(i); j <- n10 if isOdd(j) } yield i * j
    println(n10Result)

    //Test roman
    println(roman(("IV").toList)) //4
    println(roman(("IVX").toList)) //14

    //Tuples
    //returns the count, the sum, and the sum of squares
    def sumSq(in: List[Double]): (Int, Double, Double) = {
      //t is tuples so the seed is also tuple
      in.foldLeft((0, 0d, 0d))((t, v) => (t._1 + 1, t._2 + v, t._3 + v * v))
    }

    println(sumSq(List(1.0, 2.0, 3.0))) //(3. 6.0, 14.0)

    //Or use case match
    def sumSq1(in: List[Double]): (Int, Double, Double) =
      in.foldLeft((0, 0d, 0d)) {
        case ((cnt, sum, sq), v) => (cnt + 1, sum + v, sq + v * v)
      }

    println(sumSq1(List(1.0, 2.0, 3.0))) //(3. 6.0, 14.0)
    //create tuple
    val t = Pair(10, 20)
    println(t)
    println(t._2) //20
    println((2, 3, 4)._2) //3

    //Map is immutable but can reassign
    var p = Map(1 -> "David", 9 -> "Elwood")
    println(p)
    p = p + (8 -> "Archer")
    println(p) //Map(1 -> David, 9 -> Elwood, 8 -> Archer)
    println(p(9)) //"Elwood"

    //p(88)//throws NoSuchElementException
    /*get() method on Map returns an Option (Some or None)*/
    println(p.get(88)) //None
    println(p.get(9)) //Some(Elwood)

    println(p.getOrElse(99, "Nobody")) //Nobody

    //find all the values with keys between 1 and 5
    println(1 to 5 flatMap (p.get)) //Vector(David)

    //remove element in map
    p -= 9 //p = p - 9
    println(p) //Map(1 -> David, 8 -> Archer)
    println(p.contains(1)) //true

    //get largest key
    println(p.keys.reduceLeft(_ max _)) //8

    println(p.values.reduceLeft((a, b) => if (a > b) a else b)) //David

    //whether any of the values contains the letter �a�:
    println(p.values.exists(_.contains("a"))) //true

    //add a bunch of elements to a Map using the ++ method
    p ++= List(5 -> "Cat", 6 -> "Dog")
    println(p) //Map(1 -> David, 8 -> Archer, 5 -> Cat, 6 -> Dog)

    //remove a bunch of keys with the -- method
    p --= List(8, 6)
    println(p) //Map(1 -> David, 5 -> Cat)

    //Map filter demo; kv is element of map
    def removeInvalid(in: Map[String, Person]) = in.filter(kv => kv._2.valid)
    var personMap = Map("d" -> d, "e" -> e, "a" -> a)

    personMap = removeInvalid(personMap)
    println(personMap.values.map(_.first)) //List(David, Elwood)

    /*Option[T] can be either Some[T] or None. None is an object and a single instance
     * None has methods on it, so you can invoke map, flatMap, filter, foreach
     */

    /*Example call by name (=>), t is function to pass
     *  So f is function to pass, T is function returned type
     *  tryo will return Option[T]. Good example to show how to make Option
     */
    def tryo[T](f: => T): Option[T] = try { Some(f) } catch { case _: Throwable => None }

    def toInt(s: String): Option[Int] = tryo(s.toInt)
    def toBool(s: String) = tryo(JBool.parseBoolean(s))

    /*
     * Hard to understand
     * p is Map has the keys name, age; p.get return name： Option
     * 
     */
    /*
    def personFromParams(p: Map[String, String]): Option[Person] =
      for {
        name <- p.get("name")
        ageStr <- p.get("age")
        age <- toInt(ageStr)
        validStr <- p.get("valid")
        valid <- toBool(validStr)
      } yield new Person(name, age, valid)
      */

    //Retrieve the contents of an Option
    println(Some(3).get) //Int 3
    println(None.getOrElse(44)) // 44

    /*
     *  XML is immutable, Seq[Node]
     */

    var b = <b>Hello World</b>

    println(b)

    //get length for Any type of sequence
    def len(seq: Seq[_]) = seq.length
    println(len(<b>Hello</b><b>Hello</b>)) //2
    println(len(<b><span>Hello</span></b>)) //1

    /*
     * Scala code can be embedded in any attribute or element body
     * to dynamically render XML.
     */
    def now = System.currentTimeMillis.toString
    b = <b time={ now }>Hello World</b>
    println(b)

    /*
     * Attributes can be defined with Scala expressions of type String, 
     * NodeSeq, and Option[NodeSeq]
     * If the Option is None, the attribute will not be included in the resulting XML
     */
    def oddTime: Option[NodeSeq] = System.currentTimeMillis match {
      case t if isOdd(t) => Some(Text(t.toString))
      case _ => None
    }

    println(<b time={ oddTime }>Sometimes</b><b>The time is { new java.util.Date }</b>)

    /*
     * If your embedded Scala code returns a NodeSeq, the NodeSeq will be embedded directly. If
     * your Scala expression returns anything else, that thing will be converted to a scala.xml.Text
     * node by calling toString
     * It looks like the attribute need to be string or Text
     */
    b = <stuff>
          { (1 to 3).map(i => <v id={ i.toString }>#{ i }</v>) }
        </stuff>
    println(b)

    /*
     * making sure your if expressions have an else. 
     * The type of an if expression without an else is Unit, 
     * which converts to an empty String
     */

    println(<b>{ if (true) "dogs" else "" }</b>)

    val info = """var x = "";if (true && false) alert('Woof');"""
    //embed unescaped characters
    println(<script>{ PCData(info) }</script>)

    //Parse XML - it has to be well-formed xml
    //val xml = XML.load("http://www.google.com/")
    val xml = XML.loadFile("sample.xml")

    //find all the <a> tags
    println(xml \\ "a")
    println((xml \\ "a").length)

    /* '\' find all the tags with the given label (href attrib) 
    *that are direct children of the current tag <a>
    * 
    */
    val httpText = (xml \\ "a").map(_ \ "@href").map(_.text).filter(_ startsWith "http:")
    println(httpText) //List(http://xxx,...)
    val httpNumber = (xml \\ "a").filter(n => (n \ "@href").text.startsWith("http:")).length
    println(httpNumber)

    //Same as above
    val refs = for {
      a <- xml \\ "a"
      ext <- a \ "@href" if ext.text startsWith "http:"
    } yield ext.text

    println(refs)

    val x2 = <x>{ (1 to 3).map(i => <i>{ i }</i>) }</x>
    println(x2 \ "i")
    println((x2 \ "i").map(_.text)) //List(1,2,3)

    println((x2 \ "i").map(_.text.toInt).foldLeft(0)(_ + _)) //6

    /* Modify XML */

    /* RewriteRule is from Scala */
    val removeIt = new RewriteRule {
      override def transform(n: Node): NodeSeq = n match {
        case e: Elem if (e \ "@instruction").text == "remove" => NodeSeq.Empty
        case n => n
      }
    }

    val xmlBooks =
      <books instruction="update">
        <book instruction="remove" name="book1" status=""/>
        <book instruction="add" name="book2" status=""/>
      </books>

    println(new RuleTransformer(removeIt).transform(xmlBooks))

    val addIt = new RewriteRule {
      override def transform(n: Node): NodeSeq = n match {
        case e: Elem if (e \ "@instruction").text == "add" =>
          new Elem(e.prefix, e.label,
            e.attributes.remove("instruction"),
            e.scope,
            /*
             * xs: _* means xs is casted to its individual members
             * sum(args: Int*), you can pass sum(1,2,3), but you cannot pass sum(List(1,2,3)),
             * you can pass sum(List(1,2,3): _*)
             */
            transform(e.child) ++ <added>I added this</added>: _*) 
        case n => n
      }
    }
    
    println(new RuleTransformer(addIt).transform(xmlBooks))
    
    //we can pass two rules together
    println(new RuleTransformer(addIt, removeIt).transform(xmlBooks))

  }
}