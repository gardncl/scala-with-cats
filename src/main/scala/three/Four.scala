package three

import cats.Functor
import scala.language.higherKinds

object Four {

  def apply = {
    // val list = List //type constructor, takes one parameter
    def List[A] = ??? // type, produced using a type parameter
    //this is much like functions and values. Functions
    //are "value constructors"--they product values when we
    //supply parameters
    val absValue: Int = math.abs(2) //function, takes one parameter
    // value, produced using value parameter
    def absFunctionInt: Int => Int = math.abs
    def absFunctionDouble: Double => Double = math.abs
  }

  /**
    * Higher Kinds and Type Constructors
    *
    * Kinds are like types for types. They describe the
    * number of "holes" in a type.
    */

  // Declare F using underscores
  def myMethod[F[_]] = {
    // Reference F without underscores:
    // val functor = Functor.apply[F]
  }

  // Declare f specifying parameters:
  val f = (x: Int) => x * 2

  // Reference f without parameters:
  val f2 = f andThen f

}
