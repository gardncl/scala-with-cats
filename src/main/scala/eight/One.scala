package eight

import scala.concurrent.Future
import cats.instances.future._ // for Applicative
import cats.instances.list._ // for Traverse
import cats.syntax.traverse._ // for traverse
import scala.concurrent.ExecutionContext.Implicits.global
import cats.Id

object One {

  def apply() = ()

  trait UptimeClient[F[_]] {
    def getUptime(hostname: String): F[Int]
  }

  trait RealUptimeClient extends UptimeClient[Future] {
    override def getUptime(hostname: String): Future[Int]
  }

  class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
    override def getUptime(hostname: String): Int =
      hosts.getOrElse(hostname, 0)
  }

}
