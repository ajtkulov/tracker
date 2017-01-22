package model

object Cache {
  val cacheById: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByReadKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()
  val cacheByWriteKey: scala.collection.concurrent.TrieMap[String, TrackerSession] = scala.collection.concurrent.TrieMap()

  def add(session: TrackerSession): Unit = {
    cacheById.put(session.id, session)
    cacheByReadKey.put(session.id, session)
    cacheByWriteKey.put(session.id, session)
  }
}
