// package io.getquill

// import miniquill.parser._
// import scala.quoted._
// import scala.quoted.matching._
// import io.getquill.ast._

// trait CustomParserFactory extends BaseParserFactory {
//   override def userDefined(given qctxInput: QuoteContext) = Parser(new ParserComponent {
//     val qctx = qctxInput
//     def apply(root: Parser) = PartialFunction.empty[Expr[_], Ast]
//   })
// }
// object CustomParserFactory extends CustomParserFactory