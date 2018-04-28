package four

import scala.language.higherKinds

object One {

  def apply() = {
    println(stringDivideBy("6", "2"))
    println(stringDivideBy("6", "0"))
    println(stringDivideBy("6", "foo"))
    println(stringDivideBy("bar", "2"))

    // sneaky trick to create all tuples
    println(for {
      x <- (1 to 3).toList
      y <- (4 to 5).toList
    } yield (x, y))

  }


  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if(b == 0) None else Some(a / b)
  // Each of the above methods may "fail" by
  // returning none. The flatMap method allows us
  // to ignore this when we sequence operations:

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap { aNum =>
      parseInt(bStr).flatMap { bNum =>
        divide(aNum, bNum)
      }
    }

  def stringDivideByForComp(aStr: String, bStr: String): Option[Int] =
    for {
      aNum <- parseInt(aStr)
      bNum <- parseInt(bStr)
      result <- divide(aNum, bNum)
    } yield result

  /**
    * WHile we have only taked about flatMap above,
    * monadic behavior is formally captured in two
    * operations.
    *
    * pure, of type A => F[A]
    * flatMap, of type (F[A], A => F[B]) => F[B]
    *
    * pure abstracts over constructors, providing
    * a way to create a new monadic context from a
    * plain value. flatMap provides the sequencing
    * step we have already discussed, extracting the
    * value from a context and generating the next
    * context in the sequence. Here is a simplified
    * version of the Monad type class in Cats.
    */
  trait SimpleMonad1[F[_]] {
    def pure[A](value: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
  }

  /**
    * Monad laws
    *
    * 1. Left identity: calling pure and transforming
    * the result with func is the same as calling func:
    * pure(a).flatMap(func) == func(a)
    *
    * 2. Right identity: passing pure to flatMap is
    * the same as doing nothing:
    * m.flatMap(pure) == m
    *
    * 3. Associativity: flatMapping over two functions
    * f and g is the same as flatMapping over f and then
    * flatMapping over g:
    * m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
    */

  // every monad is also a functor. We can define map
  // in the same way for every monad using the existing
  // methods. flatMap and pure:

  trait SimpleMonad2[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    def map[A, B](value: F[A])(func: A => B): F[B] =
      flatMap(value)(value => pure[B](func(value)))
  }
}
