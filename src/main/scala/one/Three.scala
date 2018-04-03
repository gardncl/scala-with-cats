package one

import one.Three.PrintableSyntax._
import one.Three.Printable._

object Three {
  def apply() = {
    val cat = Cat("Biggie", 24, "Calico")
    cat.print
  }

  final case class Cat(name: String, age: Int, color: String)

  trait Printable[A] {
    def format(value: A): String
  }

  object Printable {
    implicit val stringPrintable: Printable[String] = {
      new Printable[String] {
        def format(string: String): String = string
      }
    }

    implicit val catPrintable: Printable[Cat] = {
      new Printable[Cat] {
        def format(cat: Cat): String = s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."
      }
    }
  }

  object PrintableSyntax {
    implicit class PrintableOps[A](value: A) {
      def format(implicit printable: Printable[A]): String = {
        printable.format(value)
      }

      def print(implicit printable: Printable[A]): Unit = {
        println(format(printable))
      }
    }
  }
}
