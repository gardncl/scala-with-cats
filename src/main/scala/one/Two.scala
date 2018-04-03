package one

import one.One.{JsNull, Json, JsonWriter, Person}
import one.One.JsonWriterInstances._
import one.Two.SmartImplicitList._

object Two {

  def apply() = {

    val foo = Json.toJson(Option("A string"))
    println(foo)
    // Compiler searches for implicit JsonWriter[Option[String]]
    // and finds implicit JsonWriter[Option[A]]
    val bar = Json.toJson(Option("A string"))(optionWriter[String])
    println(bar)
    // Compiler searches for JsonWriter[String] to find as parameter
    // for the above optionwriter
    val baz = Json.toJson(Option("A string"))(optionWriter(stringWriter))
    println(baz)
  }

  object BruteForceImplicitList {
    implicit val optionIntWriter: JsonWriter[Option[Int]] =
      ???

    implicit val optionPersonWriter: JsonWriter[Option[Person]] =
      ???
  }

  object SmartImplicitList {

    implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
      new JsonWriter[Option[A]] {
        def write(option: Option[A]): Json =
          option match {
            case Some(aValue) => writer.write(aValue)
            case None => JsNull
          }
      }
  }
}
