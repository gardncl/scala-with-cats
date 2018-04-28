package four

import cats.data.Reader
import cats.syntax.applicative._

object Eight {

  def apply() = {
    val garfield = Cat("Garfield", "lasagna")
    println(catName.run(garfield))

    println(greetKitty.run(garfield))

    println(greetAndFeed.run(garfield))

    val users = Map(
      1 -> "dade",
      2 -> "kate",
      3 -> "margo"
    )
    val passwords = Map(
      "dade"  -> "zerocool",
      "kate"  -> "acidburn",
      "margo" -> "secret"
    )

    val db = Db(users, passwords)
    println(checkLogin(1, "zerocool").run(db))
    println(checkLogin(4, "davinci").run(db))
  }

  case class Cat(name: String, favoriteFood: String)

  val catName: Reader[Cat, String] =
    Reader(cat => cat.name)


  // The map method simply extends the computation
  // in the reader by passing its result through a function
  val greetKitty: Reader[Cat, String] =
  catName.map(name => s"Hello, $name")

  // The flatMap method is more interesting. It allows
  // us to combine readers that depend on the same input type.
  // To illustrate this, let's extend our greeting example
  // to also feed the cat:

  val feedKitty: Reader[Cat, String] =
    Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."

  /**
    * The classic use of Readers is to build programs
    * that accept a configuration as a parameter. Let's
    * ground this with a complete example of a simple
    * login system. Our configuration will consist of two
    * databases:
    * a list of valid users
    * a list of their passwords
    */

  case class Db(
                 usernames: Map[Int, String],
                 passwords: Map[String, String]
               )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      username <- findUsername(userId)
      passwordExists <- username match {
        case None => false.pure[DbReader]
        case Some(name) => checkPassword(name, password)
      }
    } yield {
      passwordExists
    }
}
