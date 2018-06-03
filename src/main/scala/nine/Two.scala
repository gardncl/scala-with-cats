package nine

import cats.Monoid
import cats.instances.int._
import cats.instances.string._
import cats.syntax.semigroup._


object Two {

  def apply() = {
    println(foldMap[Int, Int](Vector(1, 2, 3))(identity))
    println(foldMap[Int, String](Vector(1, 2, 3))(_.toString + "!"))
    println(foldMap[Char, String]("Hello world!".toVector)(_.toString.toUpperCase))
  }

  def foldMap[A, B: Monoid](vector: Vector[A])(func: A => B): B =
    vector.foldLeft(Monoid[B].empty)(_ |+| func(_))

}

