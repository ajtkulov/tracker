import controllers.Application
import model._
import model.TrackerSessionFormatter._
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.util.Try

class ControllerTests extends PlaySpec with Results {

  "Tracking" should {
    "total data is valid" in {
      val controller = new Application()

      val res = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(res))).get
      controller.update(session.writeKey, "pavel", 123, 127).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 124, 126).apply(FakeRequest())
      controller.update(session.writeKey, "pavel1", 125, 126).apply(FakeRequest())
      println(contentAsString(controller.get(session.readKey).apply(FakeRequest())))
    }

    "last data is valid" in {
      val controller = new Application()

      val res = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(res))).get
      controller.update(session.writeKey, "pavel", 123, 127).apply(FakeRequest())
      contentAsString(controller.update(session.writeKey, "pavel1", 124, 126).apply(FakeRequest()))
      Thread.sleep(100)
      controller.update(session.writeKey, "pavel1", 125, 126).apply(FakeRequest())
      println(contentAsString(controller.getLast(session.readKey).apply(FakeRequest())))
    }

    "extend session" in {
      val controller = new Application()

      val sessionReq = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(sessionReq))).get
      controller.extendSession(session.masterKey, 127).apply(FakeRequest())
      val stateJson = contentAsString(controller.getFull(session.readKey).apply(FakeRequest()))
      val state = Json.fromJson[State](Json.parse(stateJson)).get
      assert(state.metaData.end.getMillis == 127)
    }

    "close session" in {
      val controller = new Application()

      val sessionReq = controller.createSession.apply(FakeRequest())
      val session = Json.fromJson[TrackerSession](Json.parse(contentAsString(sessionReq))).get
      controller.deleteSession(session.masterKey).apply(FakeRequest())
      val res = Try {
        contentAsString(controller.getFull(session.readKey).apply(FakeRequest()))
      }

      assert(res.isFailure)
    }
  }
}