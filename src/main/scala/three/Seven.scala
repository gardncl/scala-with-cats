package three

import cats.Contravariant
import cats.Show
import cats.instances.string._
import cats.syntax.contravariant._ // for contramap
import cats.Monoid
import cats.instances.string._
import cats.syntax.invariant._
import cats.syntax.semigroup._

object Seven {

  def apply() = {
    val showString = Show[String]
    val dave: Symbol = 'dave

    val showSymbol = Contravariant[Show].
      contramap(showString)((sym: Symbol) => s"'${sym.name}")
    showSymbol.show(dave)

    // using contramap extension method
    showString.contramap[Symbol](_.name).show(dave)

    val symbolEmptyExample = Monoid[Symbol].empty
    val symbolMonoidExample = 'a |+| 'few |+| 'words
    println(symbolEmptyExample)
    println(symbolMonoidExample)
  }

  trait SimpleContravariant[F[_]] {
    def contramap[A, B](fa: F[A])(f: B => A): F[B]
  }

  trait SimpleInvariant[F[_]] {
    def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
  }


  /**
    * Defines a monoid for Symbol by leaning on
    * the monoid for String that cats already
    * provides.
    *
    * 1. Accepts two Symbols as parameters
    * 2. Convert the Symbols to Strings
    * 3. Combine the Strings using Monoid[String]
    * 4. Convert the result back to a symbol
    */
  implicit val symbolMonoid: Monoid[Symbol] =
    Monoid[String].imap(Symbol.apply)(_.name)

}
