name := "freemoney"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa
)

libraryDependencies += "com.feth" %% "play-authenticate" % "0.8.1"
libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.5.0"
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3"

libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.9.3"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3"
libraryDependencies += "org.assertj" % "assertj-core" % "3.8.0"
libraryDependencies += "com.github.kstyrc" % "embedded-redis" % "0.6"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.4.2"
libraryDependencies += "uk.co.panaxiom" %% "play-jongo" % "2.0.0-jongo1.3"
libraryDependencies += "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.0.0"
