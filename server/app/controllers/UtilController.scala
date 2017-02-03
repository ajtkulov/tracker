package controllers

import infrastructure.FileUtils
import model._
import play.api.mvc._

import scala.util.Try

/**
  * Util controller
  */
class UtilController extends Controller {

  def info: Action[AnyContent] = Action {
    Ok(Cache.cache.size.toString)
  }

  def logs(prefix: String, lines: Int): Action[AnyContent] = Action {
    val res = Try {
      val files = FileUtils.filesInDir(s"../logs/${prefix}")
      val latest = files.sortBy(x => x.name).last.name
      FileUtils.fromFile(s"../logs/${prefix}/${latest}").toList.takeRight(lines)
    }.getOrElse(List("error"))

    Ok(res.mkString("\n"))
  }
}
