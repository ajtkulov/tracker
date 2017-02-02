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

libraryDependencies += "joda-time" % "joda-time" % "2.9.7"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.2.0"

libraryDependencies += "com.pellucid" %% "sealerate" % "0.0.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "mysql" % "mysql-connector-java" % "5.1.39",
  "org.joda" % "joda-convert" % "1.6",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
)

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" % "1.7.16"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.1"
