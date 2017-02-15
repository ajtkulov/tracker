package test

import model._
import model.TrackerSessionFormatter._
import org.joda.time.Instant
import org.scalatest.FunSuite
import play.api.libs.json.Json

class SerializationTests extends FunSuite {
  test("SerializationTest - Positions") {
    val model = Positions(Map(TimeTypes.Second -> List(Position(1, 2, new Instant()), Position(3, 2, new Instant())),
      TimeTypes.Minute -> List(Position(3, 5, new Instant()), Position(1, 7, new Instant()))))

    val json = Json.toJson(model)

    val res = Json.fromJson[Positions](json)

    assert(res.get == model)
  }

  test("SerializationTest - custom - State") {
    val model = Positions(Map(TimeTypes.Second -> List(Position(1, 2, new Instant(1485851478464L)), Position(3, 2, new Instant(1485851478464L))),
      TimeTypes.Minute -> List(Position(3, 5, new Instant(1485851478464L)), Position(1, 7, new Instant(1485851478464L)))))

    val state = State(Map("sdf" -> model))

    val json = Json.toJson(state)(TrackerSessionFormatter.customStateWriter)

    assert(json.toString() == """{"sdf":{"second":[{"lat":1,"long":2,"timestamp":1485851478464},{"lat":3,"long":2,"timestamp":1485851478464}],"minute":[{"lat":3,"long":5,"timestamp":1485851478464},{"lat":1,"long":7,"timestamp":1485851478464}]}}""")
  }
}
