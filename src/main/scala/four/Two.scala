package four

import cats.Monad
// Cats provides instances for all the monads in the
// standard library (Option, List, Vector, etc)
import cats.instances.option._ // for Monad
import cats.instances.list._
import cats.syntax.applicative._ // for pure
import cats.syntax.functor._
import cats.syntax.flatMap._
import scala.language.higherKinds

object Two {

  def apply() = {
    // Monad extends two other type classes:
    // FlatMap (provides flatMap)
    // Applicative (provides pure)
    //  * Applicative extends Functor (provides map)

    val op1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(op1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)

    println(opt3)
    val list1 = Monad[List].pure(1)
    val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
    val list3 = Monad[List].map(list2)(a => a + 123)
    println(list2)
    println(list3)

    println(1.pure[Option])
    println(1.pure[List])

    println(sumSquare(Option(3), Option(4)))
    println(sumSquare(List(1, 2, 3), List(4, 5)))
    println(sumSquareForComp(Option(3), Option(4)))
    println(sumSquareForComp(List(1, 2, 3), List(4, 5)))
  }

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x * x + y * y))

  def sumSquareForComp[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x*x + y*y

}