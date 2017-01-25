package test

import model.{Position, Positions, TimeTypes}
import model.TrackerSessionFormatter._
import org.joda.time.Instant
import org.scalatest.FunSuite
import play.api.libs.json.Json

class SerializationTests extends FunSuite {
  test("SerializationTest - Positions") {
    val model = Positions(Map(TimeTypes.Second -> List(Position(1, 2, new Instant()), Position(3, 2, new Instant()))))

    val json = Json.toJson(model)

    val res = Json.fromJson[Positions](json)

    assert(res.get == model)
  }
}
