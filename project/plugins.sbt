logLevel := Level.Warn

addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "0.10.2")

addSbtPlugin("com.github.mpeltonen" %% "sbt-idea" % "1.6.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

addSbtPlugin("com.gu" %% "sbt-teamcity-test-reporting-plugin" % "1.5")

addSbtPlugin("net.virtual-void" %% "sbt-dependency-graph" % "0.7.5")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "1.3.5")

addSbtPlugin("com.unhandledexpression" %% "sbt-clojure" % "0.1")

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" %% "sbt-plugin" % "2.5.9")

addSbtPlugin("com.github.ajtkulov" % "scala-cpd" % "0.2")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "3.2.1")

resolvers += "Flyway" at "https://flywaydb.org/repo"

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")
