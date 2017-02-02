package model

import org.joda.time.Instant
import com.github.nscala_time.time.Imports._
import dao.{SessionDto, SessionTable}

object Cache {
  val cacheByReadKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByWriteKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByMasterKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cache: scala.collection.concurrent.TrieMap[String, State] = scala.collection.concurrent.TrieMap()

  def add(session: TrackerSession): Unit = {
    cacheByReadKey.put(session.readKey, session)
    cacheByWriteKey.put(session.writeKey, session)
    cacheByMasterKey.put(session.masterKey, session)
  }

  def update(writeKey: String, user: String, position: Position): Unit = {
    val id = cacheByWriteKey(writeKey).masterKey
    val state = cache.getOrElseUpdate(id, State.empty)
    val newState = state.update(user, position)

    assert(newState.metaData.end > new Instant())

    cache.update(id, newState)
  }

  def get(readKey: String): State = {
    val id = cacheByReadKey(readKey).masterKey
    val state = cache.getOrElseUpdate(id, State.empty)

    assert(state.metaData.end > new Instant())

    state
  }

  def delete(masterKey: String): Unit = {
    val session = cacheByMasterKey(masterKey)

    cacheByWriteKey.remove(session.writeKey)
    cacheByReadKey.remove(session.readKey)
    cacheByMasterKey.remove(masterKey)
    cache.remove(session.masterKey)
  }

  def updateMetaData(masterKey: String, instant: Instant): Unit = {
    val session = cacheByMasterKey(masterKey)
    val state = cache.getOrElseUpdate(session.masterKey, State.empty)

    assert(state.metaData.end > new Instant())

    cache.update(session.masterKey, state.copy(metaData = MetaData(instant)))
  }

  def save(): Unit = {
    cache.foreach {
      case (key, state) => SessionTable.save(SessionDto(None, key, cacheByMasterKey(key), state))
    }
  }

  def load(): Unit = {
    val loaded = SessionTable.load()

    for (item <- loaded) {
      add(item._1)
      cache.put(item._1.masterKey, item._2)
    }
  }
}
