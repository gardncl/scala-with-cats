package one

import java.util.Date

import cats.Eq
import cats.syntax.eq._
import cats.syntax.option._
import cats.instances.int._
import cats.instances.long._
import cats.instances.string._
import cats.instances.option._

object Five {

  def apply() = {
    //Comparing an Option[Int] to an Int. Compiler doesn't warn you.
    val alwaysFalse = List(1, 2, 3).map(Option(_)).filter(item => item == 1)

    //Type safe equality checking
    val eqInt = Eq[Int]
    val validTruth = eqInt.eqv(123, 123)
    val validLie = eqInt.eqv(123, 234)

    //Error:(15, 34) value eq is not a member of cats.kernel.Eq[Int]
    //Note that Eq extends Any, not AnyRef.
    //Such types can participate in value classes, but instances
    //  cannot appear in singleton types or in reference comparisons.
    //val compilationError = eqInt.eq(123, "234")

    //Import interface syntax using import cats.syntax.eq._
    val validSyntaxEquality = 123 === 123
    val validSyntaxInequality = 123 =!= 234

    //Error:(28, 43) type mismatch;
    //found   : String("123")
    //required: Int
    //val anotherCompilationError = 123 === "123"

    //val needsTypeHint = Some(1) === None
    val validOptionComparison = (Some(1): Option[Int]) === (None: Option[Int])

    //extension methods imported with `import cats.syntax.option._`
    val pimpMyEquality = 1.some === none[Int]
    val pimpMyInequality = 1.some =!= none[Int]

    //using custom comparator
    val x = new Date()
    val y = new Date()

    val implicitDateCompare = x === y

    val garfield = Cat("Garfield", 24, "Black and ginger")
    val garfield2 = Cat("Garfield", 24, "Black and ginger")
    val biggie = Cat("Biggie", 24, "Calico")
    val catEquality = garfield === garfield2
    val catInequality = garfield === biggie
  }

  //this implicit for date uses `import cats.instances.long._`
  //and `import cats.syntax.eq._` to compare longs
  implicit val dateEq: Eq[Date] =
    Eq.instance[Date] { (date1, date2) =>
      date1.getTime === date2.getTime
    }

  final case class Cat(name: String, age: Int, color: String)

  implicit val catEq: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      val name = cat1.name === cat2.name
      val age = cat1.age === cat2.age
      val color = cat1.color === cat2.color
      name && age && color
    }
}
