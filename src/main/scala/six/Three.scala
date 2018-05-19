package six

import cats.Semigroupal
import cats.instances.future._ // for Semigroupal
import cats.instances.list._
import cats.instances.either._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds
import cats.Monad
import cats.syntax.flatMap._ // for flatMap
import cats.syntax.functor._ // for map

object Three {

  def apply() = {
    // start executing in parallel
    val futurePair: Future[(String, Int)] =
      Semigroupal[Future].product(Future("Hello"), Future(123))

    // Semigroupal gives us the cartesian product instead of zipping
    val listPair: List[(Int, Int)] =
      Semigroupal[List].product(List(1, 2), List(3, 4))

    // Implements fail fast rather than accumulating errors
    val eitherPair: ErrorOr[(Nothing, Nothing)] =
      Semigroupal[ErrorOr].product(Left(Vector("error 1")), Left(Vector("error 2")))

    // List and either have strange results because they are monads
    // Because of that Cats' monad provides a definition of product
    // in terms of map and flatmap
  }

  type ErrorOr[A] = Either[Vector[String], A]

  def product[M[_]: Monad, A, B](x: M[A], y: M[B]): M[(A, B)] =
    x.flatMap{ a =>
      y.map { b =>
        (a, b)
      }
    }

}
