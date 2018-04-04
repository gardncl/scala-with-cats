package two

object Four {

  def apply() = {

  }

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]) =
      monoid
  }

  /**
    * Is a monoid
    *
    * 1) Combine has type signature (A, A) => A
    * True
    * 2) An element empty of type A
    * True
    * 3) Associative
    * True
    * 4) Empty is the identity element
    * True
    */
  final class UnionMonoid[A] extends Monoid[Set[A]] {
    override def empty: Set[A] = Set.empty

    override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
  }

  /**
    * Is not a monoid
    *
    * 1) Combine has type signature (A, A) => A
    * True
    * 2) An element empty of type A
    * True
    * 3) Associative
    * False. Example:
    * ([1,2,3] - [2,3,4]) - [3,4,5] != [1,2,3] - ([2,3,4] - [4,5,6])
    * [1] != [1,2,3]
    * 4) Empty is the identity element
    * True
    */
  final class DifferenceMonoid[A] extends Monoid[Set[A]] {
    override def empty: Set[A] = Set.empty

    override def combine(x: Set[A], y: Set[A]): Set[A] = x diff y
  }

  /**
    * Is a semigroup
    *
    * 1) Combine has type signature (A, A) => A
    * True
    * 2) Associative
    * True
    *
    */
  final class IntersectionSemigroup[A] extends Semigroup[Set[A]] {
    override def combine(x: Set[A], y: Set[A]): Set[A] = x intersect y
  }

  /**
    * Is a monoid***
    *
    * 1) Combine has type signature (A, A) => A
    * True
    * 2) An element empty of type A
    * True
    * 3) Associative
    * True
    * 4) Empty is the identity element
    * True
    *
    * ***Cannot implement for all types, because it requires the identity
    * element to contain all elements of type A. Could implement for a
    * sealed trait easily, but would not (efficiently) work for Double or Int.
    */
  final class IntersectionMonoid[A] extends Monoid[Set[A]] {
    override def empty: Set[A] = Set.apply[A](/**Contains every single value in A**/)

    override def combine(x: Set[A], y: Set[A]): Set[A] = x intersect y
  }

}
