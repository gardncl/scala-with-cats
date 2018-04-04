package two

object One {

  def apply() = {
    val firstParenSubtraction = (1 - 2) - 3
    val secondParenSubtraction = 1 - (2 - 3)
    println(s"Subtraction is" +
      s"${if (firstParenSubtraction != secondParenSubtraction) " not " else " "}" +
      s"associative")
  }

  /**
    * Formally, a monoid for a type A is:
    *
    * an operation combine with type (A, A) => A
    * an element empty of type A
    */

  trait Monoid[A] {
    def combine(x: A, y: A): A

    def empty: A
  }

  /**
    * In addition to providing the combine and empty
    * operations, monoids must formally obey several laws.
    * For all values x, y, and z, in A, combine must be
    * associative and empty must be an identity element.
    */

  def associativeLaw[A](x: A, y: A, z: A)
                       (implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  def identityLaw[A](x: A)
                    (implicit m: Monoid[A]): Boolean = {
    (x == m.combine(x, m.empty)) &&
      (x == m.combine(m.empty, x))
  }

}
