package four

import cats.Eval

object Six {

  /**
    * cats.Eval allows us to abstract over different
    * methods of evaluation
    *
    * 1. Eager  happens immediately
    * 2. Lazy happens on access
    * 3. Memoized runs on first access after which
    * the results are cached
    *
    * Scala       Cats        Properties
    * val         Now         eager, memoized
    * lazy val    Later       lazy, memoized
    * def         Always      lazy, not memoized
    */
  def apply() = {
    // vals are eager and memoized
    val x = {
      println("Computing X")
      math.random()
    }
    println("X should already be computed")
    println(x) // second access
    println(x) // third access

    // defs are lazy and not memoized
    def y = {
      println("Computing Y")
      math.random()
    }
    println("Y should not get be computed yet")
    println(y) // second access
    println("Y should get computed again")
    println(y) // third access

    // lazy vals are lazy and memoized
    lazy val z = {
      println("Computing Z")
      math.random()
    }
    println("Z should not get be computed yet")
    println(z) // second access
    println(z) // third access

    // value right now like val
    val now = Eval.now(math.random + 1000)
    // lazy, memoized computation like lazy val
    val later = Eval.later(math.random + 1000)
    // lazy like def
    val always = Eval.always(math.random + 1000)

    val greeting = Eval.always { println("Step 1"); "Hello" }.map { str =>
      println("Step 2"); s"$str world"
    }
    println("Greeting should not execute yet")
    greeting.value

    // mapping functions are always called lazily on demand
    val ans = for {
      a <- Eval.now { println("Calculating A"); 40 }
      b <- Eval.now { println("Calculating B"); 2 }
    } yield {
      println("Adding A and B")
      a + b
    }
    println("A should already be calculated")
    ans.value
    println("B should get calculated again")
    ans.value

    val saying = Eval
      .always { println("Step 1"); "The cat" }
      .map { str =>
        println("Step 2"); s"$str sat on"
      }
      .memoize
      .map { str =>
        println("Step 3"); s"$str the mat"
      }

    println("Should do all steps")
    saying.value
    println("Should only do Step 3 after memoization")
    saying.value

    println(factorial(50000).value)

    println(
      foldRightStackSafe(List.range(BigInt.apply(1), BigInt.apply(50000)),
                         BigInt.apply(1))(multipleBigInt).value)
  }

  /**
    * One useful property of Eval is that its map
    * and flatMap methods are trampolined. This means
    * that we can nest calls to map and flatMap arbitrarily
    * without consuming stack frams. We call this property
    * "stack safety".
    */
  def factorialWillBlowStack(n: BigInt): BigInt =
    if (n == 1) n else n * factorialWillBlowStack(n - 1)

  def factorialStillBlowsUp(n: BigInt): Eval[BigInt] =
    if (n == 1)
      Eval.now(n)
    else
      factorialStillBlowsUp(n - 1).map(_ * n)

  /**
    * The above will still blow up because we are still
    * making all recursive calls to factorial before
    * using Eval's map method. We can get around this
    * by wrapping the computation inside of an Eval using
    * defer which defers its evaluation. Defer is also
    * a trampolined type like map and flatMap, so we
    * can use it as a quick way to make an existing
    * operation safe.
    *
    * Eval is a useful tool to enforce stack safety
    * when working on a very large computations and
    * data structures
    */
  def factorial(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(n)
    } else {
      Eval.defer(factorial(n - 1).map(_ * n))
    }

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = as match {
    case head :: tail =>
      fn(head, foldRight(tail, acc)(fn))
    case Nil =>
      acc
  }

  def foldRightStackSafe[A, B](as: List[A], acc: B)(fn: (A, B) => B): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(foldRightStackSafe(tail, acc)(fn).map(fn(head, _)))
      case Nil =>
        Eval.now(acc)
    }

  def multipleBigInt(a: BigInt, b: BigInt): BigInt = a * b

}
