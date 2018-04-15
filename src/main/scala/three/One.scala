package three

object One {

  def apply() = {
    /** Informally, a functor is anything with a map method **/
    val mappedList: List[Int] = List(1,2,3).map(n => n + 1)

    /** Map leaves the structure of the context unchanged, we can
      * call it repeatedly to sequence moltiple computations on
      * the contents of an initial data structure.
      */
    val manyMappedList: List[String] =
      mappedList.map(n => n + 1).map(n => n * 2).map(n => n + "!")

    /** We should think of map not as an iteration patter,
      * but as a way of sequencing computations on values
      * ignoring some complication dictated by th relevant
      * data type.
      *
      * Other functors:
      * Option
      * Either
      * List
      * Future
      */
  }

}
