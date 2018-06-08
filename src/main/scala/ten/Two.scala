package ten

import cats.Semigroup
import cats.instances.list._ // for Semigroup
import cats.syntax.semigroup._ // for |+|
import cats.syntax.either._ // for asLeft and asRight

object Two {

  def apply() = {
    println(check(5))
    println(check(0))
    println(checkCheck())
  }

  final case class CheckF[E, A](func: A => Either[E, A]) {
    def apply(a: A): Either[E, A] =
      func(a)

    def and(that: CheckF[E, A])(implicit s: Semigroup[E]): CheckF[E, A] =
      CheckF { a =>
        (this(a), that(a)) match {
          case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
          case (Left(e), Right(_)) => e.asLeft
          case (Right(_), Left(e)) => e.asLeft
          case (Right(a1), Right(a2)) => a.asRight
        }
      }
  }

  val a: CheckF[List[String], Int] =
    CheckF { v =>
      if(v > 2) v.asRight
      else List("Must be > 2").asLeft
    }
  val b: CheckF[List[String], Int] =
    CheckF { v =>
      if(v < -2) v.asRight
      else List("Must be < -2").asLeft
    }
  val check: CheckF[List[String], Int] =
    a and b

  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] =
      And(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] = this match {
      case Pure(func) =>
        func(a)
      case And(left, right) =>
        (left(a), right(a)) match {
          case (Left(e1),  Left(e2))  => (e1 |+| e2).asLeft
          case (Left(e),   Right(a))  => e.asLeft
          case (Right(a),  Left(e))   => e.asLeft
          case (Right(a1), Right(a2)) => a.asRight
        } }
  }
  final case class And[E, A](
                              left: Check[E, A],
                              right: Check[E, A]) extends Check[E, A]
  final case class Pure[E, A](
                               func: A => Either[E, A]) extends Check[E, A]


  val aCheck: Check[List[String], Int] =
    Pure { v =>
      if(v > 2) v.asRight
      else List("Must be > 2").asLeft
    }
  val bCheck: Check[List[String], Int] =
    Pure { v =>
      if(v < -2) v.asRight
      else List("Must be < -2").asLeft
    }
  val checkCheck: Check[List[String], Int] =
    aCheck and bCheck
}
