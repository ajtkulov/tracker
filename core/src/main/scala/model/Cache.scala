package model

object Cache {
  val cacheById: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByReadKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByWriteKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cache: scala.collection.concurrent.TrieMap[String, State] = scala.collection.concurrent.TrieMap()

  def add(session: TrackerSession): Unit = {
    cacheById.put(session.id, session)
    cacheByReadKey.put(session.readKey, session)
    cacheByWriteKey.put(session.writeKey, session)
  }

  def update(writeKey: String, user: String, position: Position): Unit = {
    val id = cacheByWriteKey(writeKey).id
    val state = cache.getOrElseUpdate(id, State.empty)
    val newState = state.update(user, position)
    cache.update(id, newState)
  }

  def get(readKey: String): State = {
    val id = cacheByReadKey(readKey).id
    cache.getOrElseUpdate(id, State.empty)
  }
}
