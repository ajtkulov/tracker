import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._
import NativePackagerHelper._

name := "server"

version := "1.0.0-SNAPSHOT"

libraryDependencies ++= Seq(cache)


libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test"
)