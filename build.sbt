import sbt._
import Keys._

scalaVersion := "2.11.7"

cancelable in Global := true

lazy val commonSettings = Seq (
  organization := "tracker",
  scalaVersion := "2.11.7",
  sources in (Compile,doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false,
  publishArtifact in (Compile, packageSrc) := false
)

lazy val root = Project(id = "tracker", base = file("."))
  .aggregate(core, server)

lazy val core = Project(id = "core", base = file("core"))
  .settings(commonSettings: _*)
  .enablePlugins(CpdPlugin)

lazy val server = Project(id = "server", base = file("server"))
  .dependsOn(core)
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.22"
)


flywaySettings

val trackerSqlUrl = settingKey[String]("trackerSqlUrl")

val trackerSqlUser = settingKey[String]("trackerSqlUser")

val trackerSqlPassword = settingKey[String]("trackerSqlPassword")

val dbPath = settingKey[String]("path to db migration scripts")

trackerSqlUser := sys.props.getOrElse("user", default = "root")

trackerSqlPassword := sys.props.getOrElse("password", default = "password")

trackerSqlUrl := sys.props.getOrElse("url", default = "jdbc:mysql://localhost:3306/tracker")

dbPath := sys.props.getOrElse("dbPath", default = "filesystem:db/")

flywayUser := trackerSqlUser.value

flywayPassword := trackerSqlPassword.value

flywayUrl := trackerSqlUrl.value

flywayLocations := Seq(dbPath.value)
