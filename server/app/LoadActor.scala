import akka.actor.Actor
import model.Cache

class LoadActor extends Actor {
  override def receive: Receive = {
    case _ => Cache.load()
  }
}
