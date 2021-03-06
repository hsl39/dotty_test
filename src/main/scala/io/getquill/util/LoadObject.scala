package io.getquill.util

import scala.reflect.ClassTag
import scala.util.Try
import scala.quoted.{Type => TType, _}

// TODO Note that this does not seem to work when .type is used directly.
// For example, in Dsl.scala, I tried using BaseParserFactory.type
// but then created a delegate trait BaseParsreFactory for the object BaseParseFactory
// which I then used directly. This worked while BaseParseFactory.type did not.
object LoadObject {
  private def `endWith$`(str: String) =
    if (str.endsWith("$")) str else str + "$"

  def applyClassTag[T](implicit tag: ClassTag[T]): Try[T] =
    Try {
      val cls = Class.forName(`endWith$`(tag.runtimeClass.getName))
      val field = cls.getField("MODULE$")
      field.get(cls).asInstanceOf[T]
    }

  def apply[T](tpe: TType[T])(given qctx: QuoteContext): Try[T] = {
    import qctx.tasty.{Try => _, given, _}
    Try {
      val className = '[$tpe].unseal.tpe.classSymbol.get.fullName
      val cls = Class.forName(`endWith$`(className))
      val field = cls.getField("MODULE$")
      field.get(cls).asInstanceOf[T]
    }
  }
}

// TODO Move this to a test
inline def loadMac[T]: String = ${ loadMacImpl[T] }
def loadMacImpl[T](given qctx: QuoteContext, tpe: TType[T]): Expr[String] = {
  val loaded = LoadObject(tpe)
  println( loaded )
  Expr(loaded.toString)
}
