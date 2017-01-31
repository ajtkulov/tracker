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

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def sessionId = column[String]("session_id")

  def session = column[TrackerSession]("session")

  def state = column[State]("state")

  def * = (id.?, sessionId, session, state) <> (SessionDto.tupled, SessionDto.unapply)
}

object SessionTable {
  val sessionTables = TableQuery[SessionTable]

  def find(sessionId: String): Seq[SessionDto] = {
    val future: Future[Seq[SessionDto]] = MySqlUtils.databaseDef.run(sessionTables.filter(x => x.sessionId === sessionId).result)

    Await.result(future, Duration.Inf)
  }
}
