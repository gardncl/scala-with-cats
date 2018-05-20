package seven

import cats.Applicative

import scala.collection.immutable
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.syntax.apply._
import cats.syntax.applicative._ // for pure
import cats.instances.future._
import cats.instances.option._ // for Applicative

import scala.language.higherKinds

object Two {

  def apply() = {

    val foldedHosts =
      Await.result(allUptimesFold, 1.second)
    println(foldedHosts)

    val totalUptime =
      Await.result(listTraverse(hostnames)(getUptime), 1.second)
    println(totalUptime)

    // wraps everything into single option
    println(process(List(2, 4, 6)))
    println(process(List(1, 3, 5)))

  }

  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )

  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60)

  val allUptimesFold: Future[List[Int]] =
    hostnames.foldLeft(Future(List.empty[Int])) {
      (accum, host) =>
        val uptime = getUptime(host)
        for {
          accum <- accum
          uptime <- uptime
        } yield accum :+ uptime
    }

  val allUptimesTraverse: Future[List[Int]] =
    Future.traverse(hostnames: List[String])(getUptime(_): Future[Int])

  /**
    * Standard library already provides traverse
    *
    * 1. Start with a List[A]
    * 2. Provide a function A => Future[B]
    * 3. End up with a Future[List[B]]
    */
  /*
  def simpleTraverse[A, B](values: List[A])
                    (func: A => Future[B]): Future[List[B]] =
    values.foldLeft(Future(List.empty[A])) { (accum, host) => val item = func(host)
      for {
        accum <- accum
        item  <- item
      } yield accum :+ item
    }
  */

  /**
    * standard library also has Future.sequence which assumes you have List[Future[B]]
    *
    * 1. Start with List[Future[A]]
    * 2. End up with a Future[List[A]]
    */
  /*
  def sequence[B](futures: List[Future[B]]): Future[List[B]] =
    traverse(futures)(identity)
   */


  // using cats we can rewrite this specific combine as a Semigroupal.combine
  def oldCombine(
                  accum: Future[List[Int]],
                  host: String
                ): Future[List[Int]] = {
    val uptime = getUptime(host)
    for {
      accum <- accum
      uptime <- uptime
    } yield accum :+ uptime
  }

  // Combining accumulator and hostname using an Applicative:
  def newCombine(accum: Future[List[Int]],
                 host: String): Future[List[Int]] =
    (accum, getUptime(host)).mapN(_ :+ _)

  // take this snippet and add it into the definition of traverse
  def listTraverse[F[_]: Applicative, A, B]
  (list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum, func(item)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  def process(inputs: List[Int]) =
    listTraverse(inputs)(n => if(n % 2 == 0) Some(n) else None)

}
