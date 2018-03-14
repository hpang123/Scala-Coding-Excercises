import scala.util.parsing.combinator._

object Calc extends JavaTokenParsers with RunParser {

  /*
   * fs is collection of function f List[Double => Double] or List[f(Double)]
   * + ~> prodExpr ^^ will match a function f
   * f is a function (x: Double) => x + d where d is prodExpr
   * f(a) will be a + d
   */
  lazy val sumExpr = prodExpr ~
    rep("+" ~> prodExpr ^^ (d => (x: Double) => x + d) |
      "-" ~> prodExpr ^^ (d => (x: Double) => x - d)) ^^ {
      case seed ~ fs => fs.foldLeft(seed)((a, f) => f(a))
    }

  /*
   * * ~> factor ^^ will match a function f - (x: Double) => x * d where d is factor
   */
  lazy val prodExpr = factor ~
    rep("*" ~> factor ^^ (d => (x: Double) => x * d) |
      "/" ~> factor ^^ (d => (x: Double) => x / d)) ^^ {
      case seed ~ fs => fs.foldLeft(seed)((a, f) => f(a))
    }

  lazy val factor: Parser[Double] =
    floatingPointNumber ^^ (_.toDouble) | "(" ~> sumExpr <~ ")" //discard  (~ and ~)

  type RootType = Double

  def root = sumExpr
}

object TestCalc {
  def main(args: Array[String]): Unit = {
    println(Calc.run("2*3")) //6.0
  }

}
