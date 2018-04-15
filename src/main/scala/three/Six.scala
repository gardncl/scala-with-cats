package three

import one.Three.Printable


object Six {

  def apply() = {

    println(format("hello"))
    println(format(true))

    println(format(Box("hello")))

    println(intCodec.encode(123))
    println(intCodec.decode("123"))

    println(encode(Box[Double](123.4)))
    println(decode[Box[Double]]("123.4"))
  }

  /**
    * Contravariant functors are type classes
    * that provide an operation called
    * contramap that representes "prepending" an
    * operation to a chain.
    *
    * A Printable[A] represents a tranformation
    * from A to String. Its contramap method accepts
    * a function func of type B => A and creates a new
    * Printable[B]
    *
    * @tparam A
    */
  trait Printable[A] {
    self =>

    def format(value: A): String

    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        def format(value: B): String = {
          self.format(func(value))
        }
      }
  }

  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      def format(value: String): String =
        "\"" + value + "\""
    }

  implicit val booleanPrintable: Printable[Boolean] =
    new Printable[Boolean] {
      def format(value: Boolean): String =
        if (value) "yes" else "no"
    }

  final case class Box[A](value: A)

  implicit def boxStringContramap[A]
  (implicit printable: Printable[A]): Printable[Box[A]] =
    printable.contramap[Box[A]](_.value)

  /**
    * Invariant functors are type classes that
    * provide an operation called imap that is
    * informally equivalent to a combination of
    * map and contramap.
    */
  trait Codec[A] {
    def encode(value: A): String
    def decode(value: String) : A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        def encode(value: B): String =
          self.encode(enc(value))

        def decode(value: String): B =
          dec(self.decode(value))
      }
    }
  }

  def encode[A](value: A)(implicit c: Codec[A]): String =
    c.encode(value)

  def decode[A](value: String)(implicit c: Codec[A]): A =
    c.decode(value)

  implicit val stringCodec: Codec[String] =
    new Codec[String] {
      def encode(value: String): String = value
      def decode(value: String): String = value
    }

  // We can construct many userful Codecs for other
  // types by building off of stringCode using imap
  implicit val intCodec: Codec[Int] =
    stringCodec.imap(_.toInt, _.toString)

  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap(_.toDouble, _.toString)

  implicit def boxCodec[A]
  (implicit codec: Codec[A]): Codec[Box[A]] =
    codec.imap[Box[A]](Box(_), _.value)
}
