package four

import cats.Id
import cats.data.{Writer, WriterT}
import cats.instances.vector._ // for Monoid
import cats.syntax.applicative._
import cats.syntax.writer._ // for tell
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Seven {

  // type alias to hide the WriterTransformer for this section
  type Writer[W, A] = WriterT[Id, W, A]

  type Logged[A] = Writer[Vector[String], A]

  def apply() = {
    // The writer monad is good for keeping track of logs while
    // doing computations so that you can append them once you've
    // received them and log them as a single object. This is
    // advantageous when doing multithreaded code so that log
    // messages don't get intertwined
    val writer: Writer[Vector[String], Int] = Writer(
      Vector(
        "It was the best of times",
        "It was the worst of times"
      ),
      1859)

    println(writer)

    // If we only have a result we can use the pure syntax
    val resultWithNoLogs: Logged[Int] = 123.pure[Logged]
    println(resultWithNoLogs)

    // If we only have logs and no result (side effects maybe?)
    // we can use the "tell" syntax
    val logsWithNoResult: Writer[Vector[String], Unit] =
      Vector("log1", "log2", "log3").tell
    println(logsWithNoResult)

    // if you have both result and a log

    val a: Writer[Vector[String], Int] =
      Writer(Vector("log1", "log2", "log3"), 123)
    val b: Writer[Vector[String], Int] =
      123.writer(Vector("log1", "log2", "log3"))

    // get the result
    println(a.value)

    // get the logs and the result
    val (log, result) = b.run
    println(log)
    println(result)

    val writer1 = for {
      a <- 10.pure[Logged]
      _ <- Vector("a", "b", "c").tell
      b <- 32.writer(Vector("x", "y", "z"))
    } yield a + b

    val (write1logs, writer1result) = writer1.run
    println(write1logs)
    println(writer1result)

    // in addition to map and flatMap we can use mapWritten
    val writer2 = writer1.mapWritten(_.map(_.toUpperCase))

    println(writer2.run)

    // can transform both log and result simultaneously
    // using bimap or mapBoth
    val writer3 = writer1.bimap(
      log => log.map(_.toUpperCase),
      res => res * 100
    )

    println(writer3.run)
    val writer4 = writer1.mapBoth { (log, res) =>
      val log2 = log.map(_ + "!")
      val res2 = res * 1000
      (log2, res2)
    }
    println(writer4.run)

    // We can clear the log with reset and swap log
    // and result with the swap method

    val writer5 = writer1.reset
    println(writer5)

    val writer6 = writer1.swap
    println(writer6)

    mixedLogFactorials


    val Vector((logA, ansA), (logB, ansB)) =
      Await.result(Future.sequence(Vector(
        Future(writerMonadFactorial(3).run),
        Future(writerMonadFactorial(5).run)
      )), 5.seconds)

    println(logA)
    println(logB)
  }

  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(100)

  def writerMonadFactorial(n: Int): Logged[Int] = {
    for {
      ans <- if(n ==0){
        1.pure[Logged]
      } else {
        slowly(writerMonadFactorial(n - 1).map(_ * n))
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans
  }

  def logFactorial(n: Int, log: String): Int = {
    slowly(if (n == 0) 1 else n * factorial(n - 1))
  }

  def factorial(n: Int): Int = {
    val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
    println(s"fact $n $ans")
    ans
  }

  def mixedLogFactorials = {
    Await.result(Future.sequence(
                   Vector(
                     Future(factorial(3)),
                     Future(factorial(3))
                   )),
                 5.seconds)
  }

}
