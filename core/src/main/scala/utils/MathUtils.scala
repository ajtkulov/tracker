package utils

object MathUtils {
  def floor(value: Long, size: Long): Long = {
    assert(value > 0)
    assert(size > 0)

    value - value % size
  }
}
