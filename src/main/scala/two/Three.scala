package two

object Three {

  /**
    * Answer: Boolean has three Monoids.
    * 1) AndBoolean
    * Combine uses the && operation
    * Empty is true
    *
    * 2) OrMonoid
    * Combine uses the || operation
    * Empty is false
    *
    * 3) EqualsMonoid
    * Combine uses the == operation
    * Empty is true
    *
    * 4) NotEqualsMonoid
    * Combine uses the != operation
    * Empty is false
    *
    */

  final val falseBoolean = false
  final val trueBoolean = true

  def apply() = {
    //Example of not a monoid
    val orMonoidBooleanTrue = new OrMonoidBooleanTrue
    if (falseBoolean ==
      orMonoidBooleanTrue.combine(falseBoolean, orMonoidBooleanTrue.empty))
      println("OrMonoidBooleanTrue is a Monoid")
    else
      println("OrMonoidBooleanTrue is not a Monoid")
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
  final class AndMonoid extends Monoid[Boolean] {
    override def empty: Boolean = true

    override def combine(x: Boolean, y: Boolean): Boolean = x && y
  }

  /**
    * Is a monoid.
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
  final class OrMonoid extends Monoid[Boolean] {
    override def empty: Boolean = false

    override def combine(x: Boolean, y: Boolean): Boolean = x || y
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
  final class EqualsMonoid extends Monoid[Boolean] {
    override def empty: Boolean = true

    override def combine(x: Boolean, y: Boolean): Boolean = x == y
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
  final class NotEqualsMonoid extends Monoid[Boolean] {
    override def empty: Boolean = true

    override def combine(x: Boolean, y: Boolean): Boolean = x != y
  }

  /**
    * Not a monoid.
    *
    * 1) Combine has type signature (A, A) => A
    * True
    * 2) An element empty of type A
    * True
    * 3) Associative
    * True
    * 4) Empty is the identity element
    * False. Counterexample:
    * combine(false, empty) => true
    */
  final class OrMonoidBooleanTrue extends Monoid[Boolean] {
    override def empty: Boolean = true

    override def combine(x: Boolean, y: Boolean): Boolean = x || y
  }

}
