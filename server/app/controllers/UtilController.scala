package controllers

import model._
import play.api.libs.json._
import play.api.mvc._

/**
  * Util controller
  */
class UtilController extends Controller {

  def info: Action[AnyContent] = Action {
    Ok(Cache.cache.size.toString)
  }
}
