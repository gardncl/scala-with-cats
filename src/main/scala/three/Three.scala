package three

import scala.language.higherKinds

object Three {

  /**
    * A function is a type F[A] with an operation
    * map that has type (A > B) => F[B] and
    * satisfies the below laws
    *
    * 1) Identity: Calling map with the identity function
    * is the same as doing nothing. eg fa.map(a => a) == fa
    *
    * 2) Composition: Mapping with two functions f and g
    * is the same as mapping with f and then mapping
    * with g. eg fa.map(g(f(_))) == fa.map(f).map(g)
    *
    * Below is a simplified version of the definition
    */
  trait SimpleFunctor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }
}
