package controllers

import java.io.File

import play.api.libs.json._
import play.api.mvc._
import scala.util._
import model._
import model.TrackerSessionFormatter._

/**
  * Application controller
  */
class Application extends Controller {

  def index: Action[AnyContent] = Action {
    Ok("it works")
  }

  def get(readKey: String): Action[AnyContent] = Action {
    Ok(Json.toJson(Cache.get(readKey)))
  }

  def update(writeKey: String, user: String, long: Double, lat: Double): Action[AnyContent] = Action {
    Cache.update(writeKey, user, Position(long, lat))
    Ok("ok")
  }

  def createSession: Action[AnyContent] = Action {
    val session = TrackerSession(StringUtils.random(), StringUtils.random(), StringUtils.random())
    Cache.add(session)
    Ok(Json.toJson(session))
  }

  def deleteSession(writeKey: String): Action[AnyContent] = Action {
    Cache.delete(writeKey)
    Ok("ok")
  }
}
