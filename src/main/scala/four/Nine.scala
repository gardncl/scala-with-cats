package four

import cats.Eval
import cats.data.{IndexedStateT, State}
import cats.syntax.applicative._

object Nine {

  def apply() = {
    val stateMonad: State[Int, String] = State[Int, String] { state =>
      (state, s"The state is $state")
    }

    // Get the state and the result
    println(stateMonad.run(10).value)

    // Get the state ignore the result
    println(stateMonad.runS(10).value)

    // Get the result ignore the state
    println(stateMonad.runA(10).value)

    val step1 = State[Int, String] { num =>
      val ans = num + 1
      (ans, s"Result of step1: $ans")
    }

    val step2 = State[Int, String] { num =>
      val ans = num * 2
      (ans, s"Result of step2: $ans")
    }

    val both: IndexedStateT[Eval, Int, Int, (String, String)] = for {
      a <- step1
      b <- step2
    } yield (a, b)

    val (state, result) = both.run(20).value
    println(state, result)

    println(evalOne("42").runA(Nil).value)

    val program1 = for {
      _ <- evalOne("1")
      _ <- evalOne("2")
      ans <- evalOne("+")
    } yield ans

    println(program1.runA(Nil).value)

    val program2 = evalAll(List("1", "2", "+", "3", "*"))
    println(program2.runA(Nil).value)

    val program3 = for {
      _ <- evalAll(List("1", "2", "+"))
      _ <- evalAll(List("3", "4", "+"))
      ans <- evalOne("+")
    } yield ans
    println(program3.runA(Nil).value)

    println(evalInput("1 2 + 3 4 + *"))
  }

  /**
    * An instance of State is a function that does two things:
    *
    * 1. transforms an input state to an output state
    * 2. computes a result
    */

  type CalcState[A] = State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] = {
    sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }
  }

  def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
      (num :: stack, num)
    }

  def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
      case a :: b :: tail =>
        val ans = func(a, b)
        (ans :: tail, ans)
      case _ =>
        sys.error("No such element!")
    }

  def evalAll(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState]) {
      (stateMonad, string) =>
        stateMonad.flatMap(_ => evalOne(string))
    }

  def evalInput(input: String): Int =
    evalAll(input.split(" ").toList).runA(Nil).value
}
