import scala.util.parsing.combinator._

object ParserCombinatorDemo  extends RegexParsers {
  def main(args: Array[String]): Unit = {
    def plus1(in: Int) = in +1
    def twice(in: Int) = in * 2
    
    /*andThen is a function and twice is passed to it
     * plus1 _ means map's element pass to plus1
     */
    var addDouble = plus1 _ andThen twice 
    println(List(1,2,3).map(addDouble)) //List(4, 6, 8)
    
    /*don't know how it works*/
    def parse = (elem('t') ~ elem('r') ~ elem('u') ~ elem('e'))
    
    
  }
}