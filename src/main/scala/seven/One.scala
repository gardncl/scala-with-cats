package seven

import cats.{Eval, Foldable, Monoid}
import cats.instances.int._
import cats.instances.list._ // for Foldable
import cats.instances.string._ // for Monoid
import cats.instances.vector._ // for Monoid

object One {

  def apply() = {
    println(show(Nil))
    println(show(List(1,2,3)))

    val list = List(1,2,3)
    // reverses the list
    println(list.foldLeft(List.empty[Int])((a: List[Int], i: Int) => i :: a))
    // list stays in order
    println(list.foldRight(List.empty[Int])((i: Int, a: List[Int]) => i :: a))

    println(foldmap(List(1, 2, 3))(_ * 2))

    println(foldFlatMap(List(1, 2, 3))(a => List(a, a * 10, a * 100)))
    println(foldFilter(List(1, 2, 3))(_ % 2 == 1))
    println(sumWithMonoid(List(1, 2, 3)))

    // foldable provides
    val ints = List(1, 2, 3)
    Foldable[List].foldLeft(ints, 0)(_ + _)

    // two methods to make use of monoids
    val sum: Int = Foldable[List].combineAll(List(1,2,3))
    println(sum)
    val concat: String = Foldable[List].foldMap(List(1,2,3))(_.toString)
    println(concat)

    val vectorList = List(Vector(1,2,3), Vector(4,5,6))

    println((Foldable[List] compose Foldable[Vector]).combineAll(vectorList))
  }

  def show[A](list: List[A]): String =
    list.foldLeft("nil")((accum, item) => s"$item then $accum")

  def foldmap[A, B](list: List[A])(func: A => B): List[B] =
    list.foldRight(List.empty[B])((i: A, a: List[B]) => func(i) :: a)

  def foldFlatMap[A, B](list: List[A])(func: A => List[B]): List[B] =
    list.foldRight(List.empty[B])((i: A, a: List[B]) => func(i) ++ a)

  def foldFilter[A](list: List[A])(func: A => Boolean): List[A] =
    list.foldRight(List.empty[A]){ (i: A, a: List[A]) =>
      if (func(i)) {
        i :: a
      } else {
        a
      }
    }

  def sumWithMonoid[A](list: List[A])
                      (implicit monoid: Monoid[A]): A = {
    list.foldRight(monoid.empty)(monoid.combine)
  }

  // Foldable defines stack safe foldRight to avoid stack overflow
//  def stackSafeFoldRight[A, B](fa: F[A], lb: Eval[B])
//                     (f: (A, Eval[B]) => Eval[B]): Eval[B]
}
