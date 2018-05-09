package five

import cats.data.EitherT
import cats.instances.future._
import cats.syntax.flatMap._
import five.Four.canSpecialMove

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Four {

  def apply() = {
    println(getPowerLevel("Jazz"))
    println(getPowerLevel("Bumblebee"))
    println(getPowerLevel("Optimus Prime"))

    println(canSpecialMove("Jazz", "Bumblebee"))
    println(canSpecialMove("Jazz", "Hot Rod"))
    println(tacticalReport("Jazz", "Hot Rod"))
  }

  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case Some(avg) => EitherT.right(Future(avg))
      case None => EitherT.left(Future(s"$autobot unreachable"))
    }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for {
      a1pl <- getPowerLevel(ally1)
      a2pl <- getPowerLevel(ally2)
    } yield a1pl + a2pl > 15

  def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value
    Await.result(stack,
      Duration(1, scala.concurrent.duration.SECONDS)) match {
      case Right(true) => "Can special move"
      case Right(false) => "Cannot special move"
      case Left(_) => "Can't find ally"
    }
  }

  // Rewrite using monad transformer

  type Response[A] = EitherT[Future, String, A]

}
