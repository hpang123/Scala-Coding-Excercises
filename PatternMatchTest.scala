

object PatternMatchTest {
  def main(argv: Array[String]) {
    val anyList = List(1, "A", 2, 2.5, 'a')
    for(m <-anyList) {
      m match {
        case i: Int => println("Integer: " + i)
        case s: String => println("String: " +s)
        case f: Double => println("Double: " +f)
        case other => println("Other: " + other)
      }
    }
    
    def getStrings(in: List[Any]): List[String] = in match {
      case Nil => Nil
      case (s: String)::rest => s::getStrings(rest)
      case _::rest => getStrings(rest)
    }
    
    println(getStrings(List(1, "hello", 'a', "ray")))
  }
}