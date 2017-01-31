package model

import org.joda.time.Instant

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
    cache.update(id, newState)
  }

  def get(readKey: String): State = {
    val id = cacheByReadKey(readKey).masterKey
    cache.getOrElseUpdate(id, State.empty)
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

    cache.update(session.masterKey, state.copy(metaData = MetaData(instant)))
  }
}
