package dao

import java.util.Properties

import slick.jdbc.JdbcBackend
import slick.driver.MySQLDriver.api._
import utils.ConfigUtils

object MySqlUtils {
  val connectionProps: Properties = {
    val res = new Properties()
    res.setProperty("autoReconnect", "true")
    res
  }

  lazy val databaseDef: JdbcBackend#DatabaseDef = Database.forURL(ConfigUtils.config().getString("storage.mysql.connection"),
    driver = "com.mysql.jdbc.Driver",
    user = ConfigUtils.config().getString("storage.mysql.user"),
    password = ConfigUtils.config().getString("storage.mysql.password"),
    prop = connectionProps)

  def withMySql[A](func: Session => A): A = {
    val session = databaseDef.createSession()
    try {
      func(session)
    } finally {
      session.close()
    }
  }
}
