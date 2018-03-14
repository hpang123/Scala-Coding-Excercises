import scala.util.parsing.combinator._

/*
 * JavaTOkenParsers is from library, it extends RegexParsers
 * RunParser is our defined trait
 * 
 * Not very understand
 */
object CalcSkel extends JavaTokenParsers with RunParser {
  lazy val sumExpr = multExpr ~ rep("+" ~ multExpr | "-" ~ multExpr) //(x*y) +- ( c*d)

  lazy val multExpr = factor ~ rep("*" ~ factor | "/" ~ factor) // (number or () ) */ (number or ())

  lazy val factor: Parser[Any] = floatingPointNumber | "(" ~ sumExpr ~ ")" //either number or ( )

  type RootType = Any

  def root = sumExpr
}

object Test {
  def main(args: Array[String]): Unit = {
    
    /*
     * rep(p) return a List
     * 
     * multExpr~List()=(factor~List())~List() = (1~List())~List()
     * 
     */
    println(CalcSkel.run("1")) //((1~List())~List())
    /*
     * multExpr~List() = (factor~List(*~factor))~List() =(2~List(*~3))~List()
     */
    println(CalcSkel.run("2*3")) //((2~List((*~3)))~List())
    
    /*
     * multExpr1~List(+~multExpr2) =(factor~List())~List((+~(factor~List())))
     * = (2~List()) ~List((+~(3~List())))
     */
    println(CalcSkel.run("2 + 3")) //((2~List())~List((+~(3~List()))))
  }
}
