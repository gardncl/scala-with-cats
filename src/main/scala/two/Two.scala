package two

object Two {

  /**
    * A semigroup is just the combine part of a monoid.
    *
    * A semigroup must be:
    *
    * Closed for its given operation.
    * eg: combining two objects of type A produces a result of type A
    *
    * Satisfies the associative property.
    * eg: (a * b) * c = a * (b * c)
    */

  /**
    * Alternate definition of Cats' Monoid using semigroup
    */

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

}
