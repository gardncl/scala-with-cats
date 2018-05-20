# Scala With Cats [![Build Status](https://travis-ci.org/gardncl/scala-with-cats.svg?branch=master)](https://travis-ci.org/gardncl/scala-with-cats)

This repo contains annotated code for my reading of _[Scala With Cats](https://underscore.io/books/scala-with-cats/)_ by Noel Walsh and Dave Gurnell.

![scala-with-cats](https://underscore.io/images/books/scala-with-cats.png)

 - [x] Chapter 1: Introduction
   - 1.1 [Anatomy of a Type Class](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/One.scala)
   - 1.2 [Working With Implicits](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/Two.scala)
   - 1.3 [Exercise: Printable Library](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/Three.scala)
   - 1.4 [Meet Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/Four.scala)
   - 1.5 [Example: Eq](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/Five.scala)
   - 1.6 [Controlling Instance Selection](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/one/Six.scala)
   - 1.7 Summary

 - [x] Chapter 2: Monoids and Semigroups
   - 2.1 [Definition of a Monoid](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/two/One.scala)
   - 2.2 [Definition of a Semigroup](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/two/Two.scala)
   - 2.3 [Exercise: The Truth About Monoids](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/two/Three.scala)
   - 2.4 [Exercise: All Set for Monoids](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/two/Four.scala)
   - 2.5 [Monoids in Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/two/Five.scala)
   - 2.6 Applications of Monoids
   - 2.7 Summary

 - [x] Chapter 3: Functors
   - 3.1 [Examples of Functors](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/One.scala)
   - 3.2 [More Examples of Functors](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Two.scala)
   - 3.3 [Definition of a Functor](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Three.scala)
   - 3.4 [Aside: Higher Kinds and Type Constructors](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Four.scala)
   - 3.5 [Functors in Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Five.scala)
   - 3.6 [Contravariant and Invariant Functors](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Six.scala)
   - 3.7 [Covariant and Invariant in Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/three/Seven.scala)
   - 3.8 Aside: Partial Unification
   - 3.9 Summary
 - [x] Chapter 4: Monads
   - 4.1 [What is a Monad?](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/One.scala)
   - 4.2 [Monads in Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Two.scala)
   - 4.3 [The Identity Monad](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Three.scala)
   - 4.4 [Either](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Four.scala)
   - 4.5 [Aside: Error Handling and MonadError](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Five.scala)
   - 4.6 [The Eval Monad](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Six.scala)
   - 4.7 [The Writer Monad](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Seven.scala)
   - 4.8 [The Reader Monad](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Eight.scala)
   - 4.9 [The State Monad](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Nine.scala)
   - 4.10 [Defining Custom Monads](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/four/Ten.scala)
   - 4.11 Summary
 - [x] Chapter 5: Monad Transformers
   - 5.1 Exercise: Composing Monads
   - 5.2 [A Transformative Example](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/five/Two.scala)
   - 5.3 [Monad Transformers in Cats](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/five/Three.scala)
   - 5.4 [Exercise: Monads: Transform and Roll Out](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/five/Four.scala)
   - 5.5 Summary
 - [x] Chapter 6: Semigroupal and Applicative
    - 6.1 [Semigroupal](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/six/One.scala)
    - 6.2 [Apply Syntax](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/six/Two.scala)
    - 6.3 [Semigroupal Applied to Different Types](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/six/Three.scala)
    - 6.4 [Validated](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/six/Four.scala)
    - 6.5 Apply and Applicative
    - 6.6 Summary
 - [x] Chapter 7: Foldable and Traverse
    - 7.1 [Foldable](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/seven/One.scala)
    - 7.1 [Traverse](https://github.com/gardncl/scala-with-cats/tree/master/src/main/scala/seven/Two.scala)
    - 7.1 Summary
 - [ ] Chapter 8: Case Study: Testing Asynchronous Code
 - [ ] Chapter 9: Case Study: Map-Reduce
 - [ ] Chapter 10: Case Study: Data Validation
 - [ ] Chapter 11: Case Study: CRDTs