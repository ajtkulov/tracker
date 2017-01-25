import controllers.Application
import model.TrackerSession
import model.TrackerSessionFormatter._

import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._


class ExampleControllerSpec extends PlaySpec with Results {

  "Tracking" should {
    "total data be valid" in {
      val controller = new Application()

      val res = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(res))).get
      controller.update(session.writeKey, "pavel", 123, 127).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 124, 126).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 125, 126).apply(FakeRequest())
      println(contentAsString(controller.get(session.readKey).apply(FakeRequest())))
    }

    "last data be valid" in {
      val controller = new Application()

      val res = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(res))).get
      controller.update(session.writeKey, "pavel", 123, 127).apply(FakeRequest())
      contentAsString(controller.update(session.writeKey, "pavel1", 124, 126).apply(FakeRequest()))
      Thread.sleep(100)
      controller.update(session.writeKey, "pavel1", 125, 126).apply(FakeRequest())
      println(contentAsString(controller.getLast(session.readKey).apply(FakeRequest())))
    }
  }
}