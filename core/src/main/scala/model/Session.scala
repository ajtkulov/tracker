package model

import scala.collection.mutable
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}

case class TrackerSession(id: String, writeKey: String, readKey: String) {}

object TrackerSessionFormatter {
  implicit val trackerSessionFormatter = Json.format[TrackerSession]
}
