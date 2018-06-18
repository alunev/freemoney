name := "freemoney"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  javaJdbc,
  ehcache,
  javaWs,
  javaJpa,
  openId,
  guice
)

libraryDependencies += "com.google.inject" % "guice" % "4.2.0"
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.20"

libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3"

libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.9.3"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3"
libraryDependencies += "org.assertj" % "assertj-core" % "3.8.0"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.4.2"
libraryDependencies += "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.0.0"

libraryDependencies += "uk.co.panaxiom" %% "play-jongo" % "2.1.0-jongo1.3"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.9" force()
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.7.9" force()
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.7.9" force()

libraryDependencies += "org.pac4j" %% "play-pac4j" % "6.0.0"
libraryDependencies += "org.pac4j" % "pac4j-oauth" % "3.0.0"

libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.3.0"

libraryDependencies += "com.google.api-client" % "google-api-client" % "1.23.0"

