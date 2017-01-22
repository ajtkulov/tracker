package model

import scala.collection.mutable
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}

case class TrackerSession(id: String, writeKey: String, readKey: String) {}

case class Position(long: Double, lat: Double) {}

case class State(values: Map[String, Position]) {
  def update(name: String, position: Position): State = {
    this.copy(values + (name -> position))
  }
}

object State {
  lazy val empty = State(Map())
}

object TrackerSessionFormatter {
  implicit val trackerSessionFormatter = Json.format[TrackerSession]
  implicit val positionFormatter = Json.format[Position]
  implicit val stateFormatter = Json.format[State]
}
