package six

import cats.instances.option._
import cats.syntax.apply._
import cats.{Monoid, Semigroupal}
import cats.instances.boolean._
import cats.instances.int._
import cats.instances.list._
import cats.instances.string._
import cats.syntax.semigroup._
import cats.instances._

object Two {

  def apply() = {
    // can use tupled for up to 22 parameters
    val tupled: Option[(Int, String)] = (Option(123), Option("abc")).tupled


    // mapN uses Semigroupal to extract the values
    // from option and then the Functor to apply
    // the values to the function
    val optionCat: Option[Cat] = (
      Option("Garfield"),
      Option(1978),
      Option("Orange & Black")
    ).mapN(Cat.apply)

    val guinness = Dog("Guinness", 2004, List("apples"))
    val rover = Dog("Rover", 1997, List("junk food"))

//    val dogs = guinness |+| rover

  }

  case class Cat(name: String, born: Int, color: String)

  case class Dog(name: String, yearOfBirth: Int, favoriteFoods: List[String])

  val tupleToDog: (String, Int, List[String]) => Dog =
    Dog.apply _

  val dogToTuple: Dog => (String, Int, List[String]) =
    dog => (dog.name, dog.yearOfBirth, dog.favoriteFoods)

//  implicit val dogMonoid: Monoid[Dog] = (
//    Monoid[String],
//    Monoid[Int],
//    Monoid[List[String]]
//  ).imapN(tupleToDog)(dogToTuple)

}
