import akka.actor.Props
import play.api.{Application, GlobalSettings}
import play.libs.Akka
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    for (actorItem <- Actors.values) {
      val jobActor = Akka.system().actorOf(actorItem.actor, name = actorItem.actorName)

      for (scheduleItem <- actorItem.schedule) {
        Akka.system().scheduler.schedule(scheduleItem.initDelay, scheduleItem.interval, jobActor, scheduleItem.message)
      }
    }
  }
}

case class ActorScheduleItem(initDelay: FiniteDuration, interval: FiniteDuration, message: Any)

case class ActorInstance(actor: Props, actorName: String, schedule: List[ActorScheduleItem]) {}

object Actors {
  val singletonMessage = ()

  val values: List[ActorInstance] = List(
    ActorInstance(Props[LoadActor], "load_actor",
      List(ActorScheduleItem(0 seconds, 100 days, singletonMessage))),
    ActorInstance(Props[SaveActor], "save_actor",
      List(ActorScheduleItem(10 minutes, 20 minutes, singletonMessage)))
  )
}
