package one

import one.One.Json

object Six {

  def apply() = {


    // List and collections are Covariant so
    // we can always substitute F[B] for F[A] where
    // B is a subtype of A
    val circles: List[Circle] = ???
    val shapes: List[Shape] = circles



    // In the case below we can use a JsonWriter[Shape]
    // any time we expect to see JsonWriter[Circle] meaning
    // that JsonWriter[Shape] <: JsonWriter[Circle]
    val shape: Shape = ???
    val circle: Circle = ???

    val shapeWriter: JsonWriter[Shape] = ???
    val circleWriter: JsonWriter[Circle] = ???


  }

  /** Covariance **/
  trait F[+A] // the "+" means "covariant"
  // Covariant means that:
  // Given: Two arbitrary types A and B
  // Given: B is a subtype of A
  // Given: An arbitrary type class F[T]
  // F is covariant if and only if F[B] is a subtype of F[A]

  sealed trait Shape
  case class Circle(radius: Double) extends Shape

  /** Contravariance **/
  trait G[-A] // the "-" means contravariant
  // Contravariant means that:
  // Given: Two arbitrary types A and B
  // Given: B is a subtype of A
  // Given: An arbitrary type class F[T]
  // F is covariant if and only if F[A] is a subtype of F[B]
  // note: last line is different

  trait JsonWriter[-A] {
    def write(value: A): Json
  }

  /** Invariance **/
  trait H[A] // absence of marker in type constructor means invariant
  //Invariant means that:
  // There does not exist a sub-type relationship
  // between any two instances of a given type class.


  /**
    * Given the below trait and classes. For invariance when he compiler searches
    * for an implicit it looks for one matching the type or subtype. THus we can use
    * variance annotations to control type class instance selection to some extend.
    * There are two issues that tend to arise.
    *
    * 1. Will an instance defined on a supertype be selected if one is available?
    * For example, can we define an instance for A and have it work for values of
    * type B and C?
    *
    * 2. Will an instance for a subtype be selected in preference to that of a supertype.
    * For instance, if we define an instance A and B, and we have a value of type B, will
    * the instance for B be selected in preference to A?
    *
    * Type class variance         Invariant     Covariant     Contravariant
    * Supertype instance used?    No            No            Yes
    * More specific type used?    No            Yes           No
    */
  sealed trait A
  final case object B extends A
  final case object C extends A

}
