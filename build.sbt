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
