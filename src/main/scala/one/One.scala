package one

import one.One.JsonWriterInstances._
import one.One.JsonSyntax._

object One {

  def apply() = {
    val foo: Json = Json.toJson(Person("Dave", "dave@example.com"))(personWriter)
    println(foo)
    val bar: Json = Person("Mike", "mike@example.com").toJson
  }

  // A type class is an interface or API that represents
  // some functionality we want to implement.

  // Define a very simple JSON AST
  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  case object JsNull extends Json

  // The "serialize to JSON" behavior is encoded in this trait
  // This is our type class
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  // Instances of a type class provide implementations
  // for the types we care about, including types
  // from the Scala standard library and types from
  // our domain model

  final case class Person(name: String, email: String)

  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] =
      new JsonWriter[String] {
        def write(value: String): Json =
          JsString(value)
      }

    implicit val personWriter: JsonWriter[Person] =
      new JsonWriter[Person] {
        def write(value: Person): Json =
          JsObject(Map(
            "name" -> JsString(value.name),
            "email" -> JsString(value.email)
          ))
      }
  }

  // A type class interface is any functionality we expose
  // to users. Interfaces are generic methods that accept
  // instances of the type class as implicit parameters.

  /**
    * The simplest way of creating an interface is to place methods
    * in a singleton object.
    *
    * To use this object we import any type class isntances we care
    * about anc dall the relevant method.
    */
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }

  /**
    * We can alternatively use extension methods to extend
    * existing types with interface methods. Cats refers to this
    * as "syntax" for the type class.
    *
    * This is the "pimp my library" pattern
    */
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json =
        w.write(value)
    }
  }




}
