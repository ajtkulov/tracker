package dao

import model.{State, TrackerSession}
import model.TrackerSessionFormatter._
import play.api.libs.json.Json
import slick.driver.MySQLDriver.api._

case class Session(id: Int, sessionId: String, session: TrackerSession, state: State) {}

class SessionTable(tag: Tag) extends Table[Session](tag, "session") {
  implicit val mapper = MappedColumnType.base[TrackerSession, String](session => Json.toJson(session).toString, json => Json.fromJson[TrackerSession](Json.parse(json)).get)
  implicit val stateMapper = MappedColumnType.base[State, String](state => Json.toJson(state).toString, json => Json.fromJson[State](Json.parse(json)).get)

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def sessionId = column[String]("session_id")

  def session = column[TrackerSession]("session")

  def state = column[State]("state")

  def * = (id, sessionId, session, state) <> (Session.tupled, Session.unapply)
}
