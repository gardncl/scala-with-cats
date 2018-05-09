package five

import cats.data.OptionT
import cats.Monad
import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure

object Two {

  def apply() = {
    // Build the ListOption from the inside out. We pass
    // a List, the type of the outer monad, as a parameter
    // to OptionT the transformer of the inner monad.
    val result1: ListOption[Int] = OptionT(List(Option(10)))

    val result2: ListOption[Int] = 32.pure[ListOption]

    // Don't have to recursively pack and unpack these
    // at each stage of the computation and the answer
    // comes back as the same type
    val addition: ListOption[Int] =
      result1.flatMap { (x: Int) =>
        result2.map { (y: Int) =>
          x + y
        }
    }
  }

  /**
    * Cats provides transformers for monads that let
    * you wrap an underlying type. The below definition
    * lets you wrap the monad of Option with a List.
    */
  type ListOption[A] = OptionT[List, A]

}
