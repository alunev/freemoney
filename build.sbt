name := "freemoney"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  javaWs
)

libraryDependencies += "com.feth" %% "play-authenticate" % "0.8.1"
libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.5.0"
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3"

libraryDependencies += "org.springframework.data" % "spring-data-mongodb" % "1.9.6.RELEASE"