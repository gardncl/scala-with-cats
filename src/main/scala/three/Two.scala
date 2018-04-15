package three

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.instances.function._ //for Functor
import cats.syntax.functor._ //for map

object Two {

  def apply() = {
    val future: Future[String] =
      Future(123)
        .map(n => n + 1)
        .map(n => n * 2)
        .map(n => n + "!")

    println(Await.result(future, 1.second))

    val mapComp = (func1 map func2)(1)

    val andThenComp = (func1 andThen func2)(1)

    val writtenOutComp = func2(func1(1))
  }

  //mapping over func1 is function composition
  val func1: Int => Double =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (y: Double) => y * 2


}
