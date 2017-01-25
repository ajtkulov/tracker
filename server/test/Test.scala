import controllers.Application
import model.TrackerSession
import model.TrackerSessionFormatter._

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import org.scalatest._
import org.scalatestplus.play._

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global


class ExampleControllerSpec extends PlaySpec with Results {

  "Transfer" should {
    "be valid" in {
      val controller = new Application()

      val res = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(res))).get
      controller.update(session.writeKey, "pavel", 123, 127).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 124, 126).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 125, 126).apply(FakeRequest())
      println(contentAsString(controller.get(session.readKey).apply(FakeRequest())))
    }
  }
}