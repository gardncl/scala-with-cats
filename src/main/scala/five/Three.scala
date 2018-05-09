package five

import cats.Id
import cats.data.{OptionT, ReaderT, StateT, WriterT}
import cats.instances.either._
import cats.syntax.applicative._
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Three {

  def apply() = {
    val a = 10.pure[ErrorOrOption]

    val b = 32.pure[ErrorOrOption]

    val c: OptionT[ErrorOr, Int] =
      a.flatMap(x => b.map(y => x + y))

    // create using apply:
    val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))

    // create using pure:
    val errorStack2 = 32.pure[ErrorOrOption]

    println(errorStack1.value)
    println(errorStack2.value.map(_.getOrElse(-1)))
//
//    val fullyWrapped: FutureEitherOption[Int] = futureEitherOption
//
//    val intermediate: FutureEither[Option[Int]] = fullyWrapped.value
//
//    val stack: Future[Either[String, Option[Int]]] = intermediate.stack
//
//    val result: Either[String, Option[Int]] =
//      Await.result(stack, Duration(1, scala.concurrent.duration.SECONDS))
  }

  // Either takes two type params, but monad only takes one
  // Alias Either to a type constructor with one parameter
  type ErrorOr[A] = Either[String, A]

  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  /**
    * Can't define a Future[Either[A,Option[B]]] because
    * we don't have too many type parameters.
    *
    * @param stack
    * @tparam F is the outer monad in the stack (Either is the inner)
    * @tparam E is the error type for the Either
    * @tparam A is the result type for the Either
    */
  case class EitherT[F[_], E, A](stack: F[Either[E, A]])

  // Have to build it up from various type classes

  type FutureEither[A] = EitherT[Future, String, A]

  type FutureEitherOption[A] = OptionT[FutureEither, A]

//  val futureEitherOption: FutureEitherOption[Int] =
//    for {
//      a <- 10.pure[FutureEitherOption]
//      b <- 10.pure[FutureEitherOption]
//    } yield a + b


  /**
    * Various monads are actually defined using the corresponding
    * transformer and Id monad. Reader, Writer, and State
    * are all defined this way.
    *
    * When the transformers are defined separately
    * to their corresponding monads they tend to
    * mirror the methods on the monad. ex: EitherT
    * defines fold, bimap, and swap.
    */

  type Reader[E, A] = ReaderT[Id, E, A]
  type Writer[W, A] = WriterT[Id, W, A]
  type State[S, A] = StateT[Id, S, A]

}
