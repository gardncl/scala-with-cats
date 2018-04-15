package three

import scala.language.higherKinds
import cats.Functor
import cats.instances.list._
import cats.instances.option._
import scala.concurrent.{Future, ExecutionContext}
import cats.instances.function._
import cats.syntax.functor._

object Five {

  def apply() = {
    val list1: List[Int] = List(1, 2, 3)
    val list2: List[Int] = Functor[List].map(list1)(_ * 2)
    val option1: Option[Int] = Option(123)
    val option2: Option[String] = Functor[Option].map(option1)(_.toString)
    val option3: Option[Int] = liftedAddOne(Option(1))
    println(option3)
//    println(func4(123))

    val optionDoMath: Option[Int] = doMath(Option(20))
    val listDoMath: List[Int] = doMath(List(1, 2, 3))
    println(optionDoMath)
    println(listDoMath)

    val tree: Tree[Int] = Branch(Tree.leaf(10), Tree.leaf(20))
    tree.map(_ * 2)
    println(tree)
    println(tree.map(_ * 2))
  }

  val addOne: Int => Int = (x: Int) => x + 1
  val liftedAddOne: Option[Int] => Option[Int] =
    Functor[Option].lift(addOne)
  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => a + "!"
  val func4 = List(1, 2, 3).map(func1).map(func2).map(func3)

  def doMath[F[_]](start: F[Int])
                  (implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  /**
    * Simplified version of FunctorOps
    */
//  implicit class FunctorOps[F[_], A](src: F[A]) {
//    def map[B](func: A => B)
//              (implicit functor: Functor[F]): F[B] =
//      functor.map(src)(func)
//  }


  // Can easily define a functor by defining its map method.
  val optionFunctor: Functor[Option] =
    new Functor[Option] {
      def map[A, B](value: Option[A])(func: A => B): Option[B] =
        value.map(func)
    }

  /**
    * Can even define implicits for types that need them like Future
    *
    * Functor[Future] -> expands to
    * Functor[Future](futureFunctor) -> expands to
    * Functor[Future](futureFunctor(executionContext))
    */
  def futureFunctor
  (implicit ec: ExecutionContext): Functor[Future] =
    new Functor[Future] {
      def map[A, B](value: Future[A])(func: A => B): Future[B] =
        value.map(func)
    }


  // Exercise 3.5.4
  sealed trait Tree[+A]

  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
      Branch(left, right)

    def leaf[A](value: A): Tree[A] =
      Leaf(value)
  }

  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  implicit val treeFunctor: Functor[Tree] =
    new Functor[Tree] {
      def map[A, B](value: Tree[A])(func: A => B): Tree[B] =
        value match {
          case Branch(left, right) =>
            Branch(map(left)(func), map(right)(func))
          case Leaf(center) => Leaf(func(center))
        }
    }

}
