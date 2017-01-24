package enum

/**
  * Indexed enums
  * @tparam T type
  */
trait IndexedEnum[T] {
  def all: Set[T]

  def valuesWithIdex: Set[(T, Int)] = all.zipWithIndex

  def toSeq: Seq[T] = all.toSeq

  def apply(idx: Int): T = toSeq(idx)

  def size: Int = all.size

  def genMap[A](func: T => A): Map[A, T] = all.map(x => (func(x), x)).toMap
}
