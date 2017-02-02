package dao

import model.TrackerSessionFormatter._
import model.{State, TrackerSession}
import play.api.libs.json.Json
import slick.driver.MySQLDriver.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class SessionDto(id: Option[Int], sessionId: String, session: TrackerSession, state: State) {}

class SessionTable(tag: Tag) extends Table[SessionDto](tag, "session") {
  implicit val mapper = MappedColumnType.base[TrackerSession, String](session => Json.toJson(session).toString, json => Json.fromJson[TrackerSession](Json.parse(json)).get)
  implicit val stateMapper = MappedColumnType.base[State, String](state => Json.toJson(state).toString, json => Json.fromJson[State](Json.parse(json)).get)

  def id = column[Int]("id", O.AutoInc)

  def sessionId = column[String]("session_id", O.PrimaryKey)

  def session = column[TrackerSession]("session")

  def state = column[State]("state")

  def * = (id.?, sessionId, session, state) <> (SessionDto.tupled, SessionDto.unapply)
}

object SessionTable {
  val sessionTables = TableQuery[SessionTable]

  def find(sessionId: String): Seq[SessionDto] = {
    Await.result(MySqlUtils.databaseDef.run(sessionTables.filter(x => x.sessionId === sessionId).result), Duration.Inf)
  }

  def save(sessionDto: SessionDto): Unit = {
    val session = Json.toJson(sessionDto.session).toString
    val state = Json.toJson(sessionDto.state).toString
    val q = sqlu"""insert into session (session_id, session, state) values (${sessionDto.session.masterKey}, ${session}, ${state})
           on duplicate key update session = ${session}, state = ${state}"""
    Await.result(MySqlUtils.databaseDef.run(q), Duration.Inf)
  }
}
