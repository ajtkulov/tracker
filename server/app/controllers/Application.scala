package controllers

import java.io.File

import play.api.libs.json._
import play.api.mvc._

import scala.util._
import model._
import model.TrackerSessionFormatter._
import org.joda.time.Instant

/**
  * Application controller
  */
class Application extends Controller {

  def index: Action[AnyContent] = Action {
    Ok("it works")
  }

  def get(readKey: String): Action[AnyContent] = Action {
    Ok(Json.toJson(Cache.get(readKey))(TrackerSessionFormatter.customStateWriter))
  }

  def getFull(readKey: String): Action[AnyContent] = Action {
    Ok(Json.toJson(Cache.get(readKey)))
  }

  def getLast(readKey: String): Action[AnyContent] = Action {
    Ok(Json.toJson(Cache.get(readKey).values.mapValues(_.last)))
  }

  def update(writeKey: String, user: String, long: Double, lat: Double): Action[AnyContent] = Action {
    Cache.update(writeKey, user, Position(long, lat, new Instant()))
    Ok("ok")
  }

  def createSession: Action[AnyContent] = Action {
    val session = TrackerSession(StringUtils.random(), StringUtils.random(), StringUtils.random())
    Cache.add(session)
    Ok(Json.toJson(session))
  }

  def deleteSession(masterKey: String): Action[AnyContent] = Action {
    Cache.delete(masterKey)
    Ok("ok")
  }

  def extendSession(masterKey: String, instant: Long): Action[AnyContent] = Action {
    Cache.updateMetaData(masterKey, new Instant(instant))
    Ok("ok")
  }

  def save(): Action[AnyContent] = Action {
    Cache.save()
    Ok("ok")
  }

  def load(): Action[AnyContent] = Action {
    Cache.load()
    Ok("ok")
  }
}
