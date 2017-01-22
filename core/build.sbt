import sbt.Package.ManifestAttributes
import scala.util._

name := "core"

version := "1.0.0-SNAPSHOT"

exportJars := true


libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"


parallelExecution in Test := false

parallelExecution in IntegrationTest := false

resolvers += "Typesafe Simple Repository" at
  "http://repo.typesafe.com/typesafe/simple/maven-releases/"

resolvers += "Typesafe Repo" at 
  "http://repo.typesafe.com/typesafe/releases/"


resolvers += "Pellucid Bintray" at "http://dl.bintray.com/pellucid/maven"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.9"
