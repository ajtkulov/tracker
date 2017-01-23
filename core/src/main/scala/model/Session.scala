package model

import org.joda.time.Instant
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}

case class TrackerSession(id: String, writeKey: String, readKey: String) {}

case class Position(long: Double, lat: Double, timestamp: Instant) {}

case class State(values: Map[String, Position]) {
  def update(name: String, position: Position): State = {
    this.copy(values + (name -> position))
  }
}

object State {
  lazy val empty = State(Map())
}

object TrackerSessionFormatter {
  implicit val dateWrites = new Writes[Instant] {
    override def writes(o: Instant): JsValue = JsNumber(o.getMillis)
  }

  implicit val dateReads = new Reads[Instant] {
    override def reads(json: JsValue): JsResult[Instant] = {
      JsSuccess(new Instant(json.as[Long]))
    }
  }

  implicit val trackerSessionFormatter = format[TrackerSession]
  implicit val positionFormatter = format[Position]
  implicit val stateFormatter = format[State]
}
