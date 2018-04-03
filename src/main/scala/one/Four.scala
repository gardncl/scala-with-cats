package one

import java.util.Date

import cats.Show
import cats.instances.int._
import cats.instances.string._
import one.Three.Cat

object Four {

  def apply() = {
    val showInt: Show[Int] = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]

    val intAsString: String = showInt.show(123)
    println(intAsString)
    val stringAsString: String = showString.show("abc")
    println(stringAsString)

    val cat = catShow.show(Cat("Biggie", 24, "Calico"))
    println(cat)
  }

  implicit val dateShow: Show[Date] =
    date => s"${date.getTime}ms since the epoch"

  implicit val catShow: Show[Cat] =
    cat => s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."

}
