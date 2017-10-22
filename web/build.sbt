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

dependencyOverrides += "org.javassist" % "javassist" % "3.20.0-GA" force()

libraryDependencies += "org.javassist" % "javassist" % "3.20.0-GA" force()
libraryDependencies += "com.impetus.kundera.client" % "kundera-redis" % "3.8"
libraryDependencies += "com.impetus.kundera.core" % "kundera-core" % "3.8" exclude("org.javassist", "javassist")
libraryDependencies += "com.impetus.kundera.core" % "fallback-impl" % "3.8" exclude("org.javassist", "javassist")
libraryDependencies += "com.feth" %% "play-authenticate" % "0.8.1"
libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.5.0"
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3"
libraryDependencies += "com.github.kstyrc" % "embedded-redis" % "0.6"

resolvers += "Kundera" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "Riptano" at "http://mvn.riptano.com/content/repositories/public"
resolvers += Resolver.sonatypeRepo("snapshots")
//resolvers += "Kundera missing" at "http://kundera.googlecode.com/svn/maven2/maven-missing-resources"
//resolvers += "Scale 7" at "https://github.com/s7/mvnrepo/raw/master"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.4.2"
libraryDependencies += "uk.co.panaxiom" %% "play-jongo" % "2.0.0-jongo1.3"
libraryDependencies += "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.0.0"
