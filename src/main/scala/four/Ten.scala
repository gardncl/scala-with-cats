package four

import cats.Monad
import scala.annotation.tailrec
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap

object Ten {

  /**
    * Custom monads can defined easily with three methods
    * 1) flatMap
    * 2) pure
    * 3) tailRecM
    */

  /**
    * Option Monad
    */
  val optionMonad = new Monad[Option] {

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa flatMap f

    /**
      * Used to limit the amount of stack space that we use
      * when composing monadic operations.
      */
    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
      f(a) match {
        case None => None
        case Some(Left(a1)) => tailRecM(a1)(f)
        case Some(Right(b)) => Some(b)
      }

    override def pure[A](x: A): Option[A] =
      Some(x)
  }

  implicit val treeMonad = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Branch(left, right) =>
          Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) =>
          f(value)
      }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      f(a) match {
        case Branch(l, r) =>
          Branch(
            flatMap(l) {
              case Left(l) => tailRecM(l)(f)
              case Right(l) => pure(l)
            },
            flatMap(r) {
              case Left(r) => tailRecM(r)(f)
              case Right(r) => pure(r)
            }
          )
        case Leaf(Left(value)) => tailRecM(value)(f)
        case Leaf(Right(value)) => Leaf(value)
      }

    override def pure[A](x: A): Tree[A] = Leaf(x)
  }

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A])
    extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  def apply() = {
    val tree = branch(leaf(100), leaf(200)).
      flatMap(x => branch(leaf(x - 1), leaf(x + 1)))

    println(tree)

    val forTree = for {
      a <- branch(leaf(100), leaf(200))
      b <- branch(leaf(a - 10), leaf(a + 10))
      c <- branch(leaf(b - 1), leaf(b + 1))
    } yield c

    println(forTree)
  }

}


