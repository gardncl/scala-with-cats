package two

import cats.Monoid
import cats.Semigroup
import cats.syntax.eq._
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.syntax.semigroup._

object Five {

  def apply() = {
    val combinedStringFromMonoid: String = Monoid[String].combine("Hi ", "there")
    val emptyString: String = Monoid[String].empty
    val combinedStringFromSemigroup: String = Semigroup[String].combine("Hi ", "there")
    if (combinedStringFromMonoid === combinedStringFromSemigroup)
      println("monoid and semigroup work the same way for combining strings")
    else
      println("monoid and semigroup work differently for combining strings")

    val combinedInts: Int =
      Monoid[Int].combine(32, 10)

    val optionCombinedInts: Option[Int] =
      Monoid[Option[Int]].combine(Option(22), Option(20))

    //Can also use cats.syntax.semigroup._ for prettier syntax
    val stringResult = "Hi " |+| "there" |+| Monoid[String].empty

    val intResult = 1 |+| 2 |+| Monoid[Int].empty

    val order1 = Order(1,2)
    val order2 = Order(3,6)
    val order3 = Order(9,18)
    val orderList = List(order1, order2, order3)
    val superOrder = OrderSuperAdder.add(orderList)
    println(superOrder)
  }


  //Exercise 2.5.4
  case class Order(totalCost: Double, quantity: Double)

  implicit val monoid: Monoid[Order] = new Monoid[Order] {
    override def combine(x: Order, y: Order): Order =
      Order(x.totalCost + y.totalCost, x.quantity + y.quantity)

    override def empty: Order = Order(0,0)
  }

  trait SuperAdder[T] {
    def add(items: List[T])
                    (implicit monoid: Monoid[T]): T =
    items.foldLeft(monoid.empty)((a, b) => a |+| b)
  }

  object IntegerSuperAdder extends SuperAdder[Int]
  object OrderSuperAdder extends SuperAdder[Order]

}
