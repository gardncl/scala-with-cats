package four

import cats.syntax.either._

object Four {

  def apply() = {
    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(32)

    // Either is right biased so you can map
    // based on only the right values
    val combineEithers = for {
      a <- either1
      b <- either2
    } yield a + b

    println(combineEithers)

    // extension methods to avoid having to
    // type out the entire Either syntax when defining one.
    // Automatically widens the Right/Left value to type
    // Either so you can avoid type errors (expected Either got Right)
    // Especially nice since Either is Covariant
    val a = 3.asRight[String]
    val b = 4.asRight[String]

    val noTypeAnnotations = for {
      x <- a
      y <- b
    } yield x * x + y * y

    println(noTypeAnnotations)

    println(countPositive(List(1, 2, 3)))
    println(countPositive(List(1, -2, 3)))

    // cats.syntax.either also gives you access to a couple
    // useful extension methods
    println(Either.catchOnly[NumberFormatException]("foo".toInt))
    println(Either.catchNonFatal(sys.error("Badness")))

    // Create eithers from other data structures
    println(Either.fromTry(scala.util.Try("foo".toInt)))
    println(Either.fromOption[String, Int](None, "Badness"))

    // Can also return defaults on failure
    println("Error".asLeft[Int].getOrElse(0))
    println("Error".asLeft[Int].orElse(2.asRight[String]))

    // Ensure allows us to check if Right(_) satisfies a predicate
    println(-1.asRight[String].ensure("Must be non-negative")(_ > 0))

    // recover and recoverWith provide similar error handling
    println("error".asLeft[Int].recover {
      case str: String => 1
    })

    println("error".asLeft[Int].recoverWith{
      case str: String => Right(-1)
    })

    // leftMap and bimap methods to complement map
    println("foo".asLeft[Int].leftMap(_.reverse))
    println(6.asRight[String].bimap(_.reverse, _ * 7))
    println("bar".asLeft[Int].bimap(_.reverse, _ * 7))

    // the swap method lets us exchange left for right
    println(123.asRight[String])
    println(123.asRight[String].swap)

    // also methods for converting to other data types:
    // toOption, toList, toTry, toValidated


    // fail fast error handling
    val failFast = for {
      a <- 1.asRight[String]
      b <- 0.asRight[String]
      c <- if(b == 0) "DIV0".asLeft[Int]
        else (a / b).asRight[String]
    } yield c * 100
    println(failFast)
  }

  /**
    * // <console>:21: error: type mismatch;
    * // found : scala.util.Either[Nothing,Int] // required: scala.util.Right[Nothing,Int] // accumulator.map(_ + 1)
    * // ^
    * // <console>:23: error: type mismatch;
    * // found : scala.util.Left[String,Nothing] // required: scala.util.Right[Nothing,Int] // Left("Negative. Stopping!") // ^
    *
    * @param nums
    * @return
    *
    * This code fails to compile for two reasons:
    * 1. Compiler infers the narrow type of Right instead
    * of the widened type Either
    * 2. Didn't specify the type for Left so the it was
    * inferred to be Nothing
    *
  def countPositive(nums: List[Int]) =
    nums.foldLeft(Right(0)) { (accumulator, num) =>
      if (num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
    }
    */

  def countPositive(nums: List[Int]) = nums.foldLeft(0.asRight[String]) {
    (accumulator, num) =>
      if (num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
  }

}
