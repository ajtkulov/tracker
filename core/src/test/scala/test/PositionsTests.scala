package test

import model.{Position, Positions, TimeTypes}
import org.joda.time.DateTime
import org.scalatest.FunSuite

class PositionsTests extends FunSuite {
  test("PositionsTest - compress - 1") {
    val initDate = new DateTime(2000, 1, 2, 4, 1, 45)
    val points = (0 to 100).map(x => Position(x, x, initDate.plusSeconds(x * 30).toInstant))

    val input = Positions(Map(TimeTypes.Second -> points))

    val res = input.compress()

    assert(res.values(TimeTypes.Hour).exists(_.timestamp == initDate.toInstant))
    assert(res.values(TimeTypes.Hour).size == 1)

    val min15 = new DateTime(2000, 1, 2, 4, 15, 15)
    assert(res.values(TimeTypes.FifteenMinutes).exists(_.timestamp == initDate.toInstant))
    assert(res.values(TimeTypes.FifteenMinutes).exists(_.timestamp == min15.toInstant))
    assert(res.values(TimeTypes.FifteenMinutes).exists(_.timestamp == min15.plusMinutes(15).toInstant))
    assert(res.values(TimeTypes.FifteenMinutes).exists(_.timestamp == min15.plusMinutes(30).toInstant))
    assert(res.values(TimeTypes.FifteenMinutes).size == 4)

    assert(res.values.forall(x => x._2.size <= 6))

    val lastSec = new DateTime(2000, 1, 2, 4, 51, 45)

    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.toInstant))
    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.minusSeconds(30).toInstant))
    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.minusSeconds(60).toInstant))
    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.minusSeconds(90).toInstant))
    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.minusSeconds(120).toInstant))
    assert(res.values(TimeTypes.Second).exists(_.timestamp == lastSec.minusSeconds(150).toInstant))
  }
}
