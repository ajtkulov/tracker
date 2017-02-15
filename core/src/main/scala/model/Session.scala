package model

import model.State.User
import org.joda.time.Instant
import play.api.libs.json.Json._
import play.api.libs.json.{Json, _}
import com.github.nscala_time.time.Imports._
import scala.collection.immutable.Seq

case class TrackerSession(masterKey: String, writeKey: String, readKey: String) {}

case class Position(lat: Double, long: Double, timestamp: Instant) {}

case class Positions(values: Map[TimeType, Seq[Position]]) {
  def add(value: Position): Positions = {
    val seconds = values(TimeTypes.Second) :+ value
    Positions(values.updated(TimeTypes.Second, seconds))
  }

  def last: Position = {
    values(TimeTypes.Second).maxBy(_.timestamp)
  }

  def compress(sizePerLevel: Int = 6): Positions = {
    val flatten: Seq[Position] = values.flatMap(x => x._2).toList

    val normalized: Map[TimeType, List[Position]] = TimeTypes.order.map(timeType => {
      val floor: Seq[(Position, Instant)] = flatten.map(x => (x, timeType.floor(x.timestamp)))
      (timeType, floor.groupBy(_._2).mapValues(x => x.minBy(_._1.timestamp)._1).values.toList )
    }).toMap

    Positions(normalized.mapValues(x => x.sortBy(_.timestamp)(Ordering[Instant].reverse).take(sizePerLevel)))
  }
}

object Positions {
  lazy val empty = Positions(Map().withDefaultValue(List()))
}

case class MetaData(end: Instant) {}

case class State(values: Map[User, Positions], point: Option[Position] = None, updates: Int = 0, metaData: MetaData = MetaData.empty) {
  def update(name: User, position: Position): State = {
    val positions = values.getOrElse(name, Positions.empty)
    val newPositions = positions.add(position)
    val res = this.copy(values + (name -> newPositions), updates = updates + 1)
    if (updates < 20) {
      res
    } else {
      this.copy(res.values.mapValues(x => x.compress()))
    }
  }
}

object State {
  type User = String

  def empty = State(Map(), None, 0, MetaData.empty)
}

object MetaData {
  def empty = MetaData(new Instant().plus(24 hours))
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

  implicit val positionsFormatter = new Format[Positions] {
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

  val customPositionsWriter = new Writes[Positions] {
    override def writes(o: Positions): JsValue = {
      JsObject(o.values.toList.map(x => x._1.name -> Json.toJson(x._2)))
    }
  }

  val customStateWriter = new Writes[State] {
    override def writes(o: State): JsValue = {
      JsObject(o.values.map(x => x._1 -> Json.toJson(x._2)(customPositionsWriter)).toList)
    }
  }

  implicit val trackerSessionFormatter = format[TrackerSession]
  implicit val metaDataFormatter = format[MetaData]
  implicit val stateFormatter = format[State]
}
