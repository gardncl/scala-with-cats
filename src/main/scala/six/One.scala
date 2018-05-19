package six

import cats.Semigroupal
import cats.instances.option._

object One {

  def apply() = {
    val combinedOptions: Option[(Int, String)] = Semigroupal[Option].product(Some(123), Some("abc"))
    println(combinedOptions)
    // If either entry is None the whole result is None
    val combinedOptionsWithNone: Option[(Int, String)] = Semigroupal[Option].product(None, Some("abc"))
    println(combinedOptionsWithNone)

    val combineThree = Semigroupal.tuple3(Option(1),Option(2),Option(3))
    println(combineThree)

    val combineThreeNone = Semigroupal.tuple3(Option(1),Option(2),Option.empty[Int])
    println(combineThreeNone)

    val combinedThree = Semigroupal.map3(Option(1),Option(2),Option(3))(_ + _ + _)
    println(combinedThree)

    val combinedTwo = Semigroupal.map2(Option(1),Option.empty[Int])(_ + _)
    println(combinedTwo)
  }


  /**
    * Type class that allows us to combine contexts.
    * Given F[A] and F[B] we can produce F[(A,B)].
    *
    * The F[A] and F[B] given as parameters are independent
    * of each other so there is no ordering like with flatMap.
    *
    * @tparam F
    */
  trait SimpleSemigroupal[F[_]] {
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  }

}
