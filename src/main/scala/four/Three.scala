package four

import cats.{Id, Monad}
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap

object Three {

  def apply() = {
    // The Id monad allows you to call
    // monadic methods with plain params
    Two.sumSquare(3: Id[Int], 4: Id[Int])

    val string: Id[String] = "Dave": Id[String]
    val int: Id[Int] = 123: Id[Int]
    val list: Id[List[Int]] = List(1, 2, 3): Id[List[Int]]

    println(string)
    println(int)
    println(list)

    // Cats provides instances of various type classes
    // for Id, including Functor and Monad. These let
    // us call map, flatMap, and pure passing in plain
    // values
    val a: Id[Int] = Monad[Id].pure(3)
    val b: Id[Int] = Monad[Id].flatMap(a)(_ + 1)

    val c: Id[Int] = for {
      x <- a
      y <- b
    } yield x + y

    val pureEx: Id[Int] = pureExercise(123)
    val mapEx: Id[String] = mapExercise(a)(_.toString)
    val flatMapEx: Id[Int] = flatMapExercise(a)(_ + 1)
    println(pureEx)
    println(mapEx)
    println(flatMapEx)
  }


  /**
    * Once we strip away the Id type constructors
    * flatMap and map are actually identical.
    * This ites in with our understanding of
    * functors and monads as sequencing type
    * classes. Each type class allows us to sequence
    * operations ignoring some kind of complication.
    * In the case of Id there is no complication,
    * making map and flatMap the same thing.
    */
  def pureExercise[A](a: A): Id[A] =
    a

  def mapExercise[A, B](a: Id[A])(func: A => B): Id[B] =
    func(a)

  def flatMapExercise[A, B](a: Id[A])(func: A => Id[B]): Id[B] =
    func(a)

}
