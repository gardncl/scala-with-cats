package eight

import scala.concurrent.Future
import cats.instances.future._
import cats.instances.list._
import cats.syntax.traverse._
import One._
import cats.Applicative
import cats.syntax.functor._ // for map

object Two {

  def apply() = {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    println(actual)
    println(expected)
    assert(actual == expected)
  }

  class UptimeService[F[_]: Applicative](client: UptimeClient[F]) {
    def getTotalUptime(hostnames: List[String]): F[Int] =
      hostnames.traverse(client.getUptime).map(_.sum)
  }

}
