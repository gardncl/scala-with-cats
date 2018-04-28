package four

import cats.Monad
import cats.MonadError
import cats.instances.either._
import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.monadError._
import cats.instances.try_._

import scala.util.Try

object Five {

  type ErrorOr[A] = Either[String, A]

  def apply() = {
    val monadError = MonadError[ErrorOr, String]
    val success = monadError.pure(42)
    // raiseError is like the pure method for Monad
    // except that it creates an instance representing
    // a failure
    val failure = monadError.raiseError("Badness")
    println(success)
    println(failure)

    // handleError is the complement of RaiseError. It
    // allows us to consume an error and (possibly) turn it
    // into a success, similar to the recover method of Future
    val handled = monadError.handleError(failure) {
      case "Badness" =>
        monadError.pure("It's ok")
      case _ =>
        monadError.raiseError("It's not okay")
    }
    println(handled)


    // Ensure specifies an error to raise if the
    // predicate returns false. This can be used
    // to filter
    println(
      monadError.ensure(success)("Number too low!")(_ > 1000)
    )

    // Cats provides syntax via cats.syntax.monadError._
    val successWithSyntax = 42.pure[ErrorOr]
    val failureWithSyntax = "Badness".raiseError[ErrorOr, Int]
    println(successWithSyntax)
    println(failureWithSyntax)
    println(successWithSyntax.ensure("Number too low!")(_ > 1000))


    // Cats provides instances of MonadError for numerous data
    // types including Either, Future, and Try. The instance
    // of Either is customizable to any error type, whereas
    // the instances for Future and Try always represent errors
    // as throwables.
    val exn: Throwable =
      new RuntimeException("Hold onto your butts")

    println(exn.raiseError[Try, Int])
  }

  /**
    * Cats provides MonadError that abstracts over Either-like
    * data types that are being used to handle errors.
    * MonadError provides extra operations for raising and
    * handling errors.
    *
    * @tparam F is the type of the monad
    * @tparam E is the type of the error contained within F
    */
  trait SimpleMonadError[F[_], E] extends Monad[F] {
    // Lift an error into the 'F' context:
    def raiseError[A](a: E): F[A]

    // Handle an error, potentially recovering from it:
    def handleError[A](fa: F[A])(f: E => A): F[A]

    // Test an instance of 'F',
    // failing if the predicate is not satisfied
    def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
  }

}
