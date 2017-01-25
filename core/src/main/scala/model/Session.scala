package model

import model.State.User
import org.joda.time.Instant
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}
import com.github.nscala_time.time.Imports._

case class TrackerSession(id: String, writeKey: String, readKey: String) {}

case class Position(long: Double, lat: Double, timestamp: Instant) {}

case class Positions(values: Map[TimeType, Seq[Position]]) {
  def add(value: Position): Positions = {
    val seconds = values(TimeTypes.Second) :+ value
    Positions(values.updated(TimeTypes.Second, seconds))
  }

  def last: Position = {
    values(TimeTypes.Second).maxBy(_.timestamp)
  }
}

object Positions {
  lazy val empty = Positions(Map().withDefaultValue(List()))
}

case class State(values: Map[User, Positions]) {
  def update(name: User, position: Position): State = {
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
    override def writes(o: TimeType): JsValue = JsString(o.name)
  }

  implicit val timeTypeReads = new Reads[TimeType] {
    override def reads(json: JsValue): JsResult[TimeType] = {
      JsSuccess(TimeTypes.getByName(json.as[String]))
    }
  }

  implicit val positionFormatter = format[Position]

  implicit val positionsReads = new Format[Positions] {
    override def writes(o: Positions): JsValue = {
      obj("positions" -> o.values.toList.map(x => obj(x._1.name -> x._2)))
    }

    override def reads(json: JsValue): JsResult[Positions] = {
      JsSuccess(Positions((json \ "positions").as[JsArray].value.flatMap(x => {
        x.as[JsObject].fields.map(x => {
          (TimeTypes.getByName(x._1), x._2.as[Seq[Position]])
        })
      }).toMap))
    }
  }

  implicit val trackerSessionFormatter = format[TrackerSession]
  implicit val stateFormatter = format[State]
}
