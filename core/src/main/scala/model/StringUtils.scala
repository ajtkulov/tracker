package model

import java.security.MessageDigest

import scala.util.Random

object StringUtils {
  def md5(string: String): String = {
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(string.getBytes).map("%02x".format(_)).mkString
  }

  def random(): String = {
    md5(Random.self.nextLong().toString)
  }
}
