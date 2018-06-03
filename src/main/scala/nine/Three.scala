package nine

import cats.Monoid
import cats.instances.future._
import cats.instances.int._
import cats.instances.vector._
import cats.syntax.semigroup._
import cats.syntax.traverse._
import nine.Two.foldMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Three {

  def apply() = {
    val result: Future[Int] =
      parallelFoldMap((1 to 1000000).toVector)(identity)

    println(Await.result(result, Duration.Inf))
  }

  def parallelFoldMap[A, B: Monoid]
  (values: Vector[A])
  (func: A => B): Future[B] = {
    val numCores: Int = Runtime.getRuntime.availableProcessors
    val groupSize = (values.size.toFloat / numCores).ceil.toInt

    val groups: Iterator[Vector[A]] =
      values.grouped(groupSize)

    val futures: Iterator[Future[B]] =
      groups map { group =>
        Future {
          foldMap(group)(func)
        }
      }

    Future.sequence(futures) map { iterable =>
      iterable.foldLeft(Monoid[B].empty)(_ |+| _)
    }
  }

  def parallelFoldMapWithCats[A, B: Monoid]
  (values: Vector[A])
  (func: A => B): Future[B] = {
    val numCores: Int = Runtime.getRuntime.availableProcessors
    val groupSize = (values.size.toFloat / numCores).ceil.toInt

    values
      .grouped(groupSize)
      .toVector
      .traverse[Future, B](group => Future(foldMap[A, B](group)(func)))
      .map(vector => vector.foldLeft(Monoid[B].empty)(_ |+| _))
  }
}
