package six

import java.io

import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._
import cats.syntax.validated._
import cats.syntax.applicative._
import cats.syntax.applicativeError._ // for raiseError
import cats.instances.list._
import cats.syntax.apply._

object Four {

  type AllErrorsOr[A] = Validated[List[String], A]

  def apply() = {

    //Validated complements Either because we have fail-fast
    // as well as accumulating error handling
    val errors: AllErrorsOr[(Nothing, Nothing)] = Semigroupal[AllErrorsOr].product(
      Validated.invalid(List("Error 1")),
      Validated.invalid(List("Error 2"))
    )
    println(errors)

    // Two subtypes invalid and valid which are pretty much
    // left and right from either, respectively
    val valid: Validated.Valid[Int] =
    Validated.Valid(123)
    println(valid)

    val invalid: Validated.Invalid[List[String]] =
      Validated.Invalid(List("badness"))
    println(invalid)

    // We can widen these types as well
    val validWidened: Validated[List[String], Int] =
      Validated.valid[List[String], Int](123)
    println(validWidened)

    val invalidWidened: Validated[List[String], Int] =
      Validated.invalid[List[String], Int](List("Badness"))
    println(invalidWidened)

    // with cats.syntax.validated._ imported we can use extension methods
    val validExtension = 123.valid[List[String]]
    println(validExtension)

    val invalidExtension = List("badness").invalid[Int]
    println(invalidExtension)

    // can use pure and raise error from applicative and applicativeError
    val validApplicative: AllErrorsOr[Int] =
      123.pure[AllErrorsOr]
    println(validApplicative)

    val invalidApplicationError: AllErrorsOr[Int] =
      List("badness").raiseError[AllErrorsOr, Int]
    println(invalidApplicationError)

    // There are various helper methods to create instances
    // of Validated from different sources as to create them
    // from Try, Either, and Option.

    val catchOnly: Validated[NumberFormatException, Int] =
      Validated.catchOnly[NumberFormatException]("foo".toInt)

    val catchNonFatal: Validated[Throwable, Nothing] =
      Validated.catchNonFatal(sys.error("Badness"))

    val fromTry: Validated[Throwable, Int] =
      Validated.fromTry(scala.util.Try("foo".toInt))

    val fromEither: Validated[String, Int] =
      Validated.fromEither[String, Int](Left("Badness"))

    val fromOption: Validated[String, Int] =
      Validated.fromOption[String, Int](None, "Badness")

  }

  case class User(name: String, age: Int)

  def readName(kv: Map[String, String]): Either[String, String] = {
    val nameOption: Option[String] = kv.get("name")
    nameOption match {
      case None => Left("No name present")
      case Some(name) if name.isEmpty => Left("Empty name")
      case Some(name) => Right(name)
    }
  }

  def readAge(kv: Map[String, String]): Either[String, Int] = {
    val ageOption: Option[Int] = kv.get("age").map(Integer.parseInt(_))
    ageOption match {
      case None => Left("No age present")
      case Some(age) if age < 0 => Left("Negative age")
      case Some(age) => Right(age)
    }
  }

//  def readUser(data: Map[String, String]) = {
//    (
//      Validated.fromEither[String, String](readName(data)),
//      Validated.fromEither[String, Int](readAge(data))
//    ).mapN(User.apply)
//  }


}
