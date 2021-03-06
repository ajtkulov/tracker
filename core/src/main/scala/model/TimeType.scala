package model

import com.pellucid.sealerate
import enum.IndexedEnum
import org.joda.time.Instant
import utils.MathUtils

sealed abstract class TimeType(val name: String, val length: Int) extends Ordered[TimeType] {
  def compare(that: TimeType): Int = this.length compare that.length

  def floor(value: Instant): Instant = {
    new Instant(MathUtils.floor(value.getMillis, length * 1000))
  }
}

object TimeTypes extends IndexedEnum[TimeType] {

  case object Second extends TimeType("second", 1)

  case object Minute extends TimeType("minute", 60)

  case object FiveMinutes extends TimeType("5minutes", 5 * 60)

  case object FifteenMinutes extends TimeType("15minutes", 15 * 60)

  case object Hour extends TimeType("hour", 60 * 60)

  override def all: Set[TimeType] = sealerate.values[TimeType]

  lazy val mapByLength = genMap(x => x.length)

  lazy val mapByName = genMap(x => x.name)

  def getByLength(length: Int): TimeType = mapByLength(length)

  def getByName(name: String): TimeType = mapByName(name)

  lazy val order: List[TimeType] = all.toList.sortBy(_.length)
}
