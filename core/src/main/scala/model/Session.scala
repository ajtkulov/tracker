package model

import model.State.User
import org.joda.time.Instant
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}

case class TrackerSession(id: String, writeKey: String, readKey: String) {}

case class Position(long: Double, lat: Double, timestamp: Instant) {}

case class Positions(values: Map[TimeType, Seq[Position]]) {
  def add(value: Position): Positions = {
    val seconds = values(TimeTypes.Second) :+ value
    Positions(values.updated(TimeTypes.Second, seconds))
  }
}

object Positions {
  lazy val empty = Positions(Map().withDefaultValue(List()))
}

case class State(values: Map[String, Positions]) {
  def update(name: String, position: Position): State = {
    val positions = values.getOrElse(name, Positions.empty)
    val newPositions = positions.add(position)
    this.copy(values + (name -> newPositions))
  }
}

object State {
  type User = String

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

  implicit val timeTypeWrites = new Writes[TimeType] {
    override def writes(o: TimeType): JsValue = JsNumber(o.length)
  }

  implicit val timeTypeReads = new Reads[TimeType] {
    override def reads(json: JsValue): JsResult[TimeType] = {
      JsSuccess(TimeTypes.getByLength(json.as[Int]))
    }
  }

  implicit val positionFormatter = format[Position]

  implicit val positionsReads = new Format[Positions] {
    override def writes(o: Positions): JsValue = arr(
      o.values.toList.map(x => obj(x._1.name -> x._2))
    )

    override def reads(json: JsValue): JsResult[Positions] = ???
  }

  implicit val trackerSessionFormatter = format[TrackerSession]
  implicit val stateFormatter = format[State]
}
