import akka.actor.Actor
import model.Cache

class SaveActor extends Actor {
  override def receive: Receive = {
    case _ => Cache.save()
  }
}
