import scala.util.parsing.combinator._

trait RunParser {
  this: RegexParsers => //RunParser require RegexParsers trait

  type RootType

  def root: Parser[RootType] //return Parser that is from library

  def run(in: String): ParseResult[RootType] = parseAll(root, in) //parseAll is from library
}
