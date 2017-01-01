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

libraryDependencies += "com.impetus.kundera.client" % "kundera-redis" % "3.7"
libraryDependencies += "com.impetus.kundera.core" % "fallback-impl" % "3.7"
libraryDependencies += "com.feth" %% "play-authenticate" % "0.8.1"

libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3"
libraryDependencies += "com.github.kstyrc" % "embedded-redis" % "0.6"

resolvers += "Kundera" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "Riptano" at "http://mvn.riptano.com/content/repositories/public"
resolvers += Resolver.sonatypeRepo("snapshots")
//resolvers += "Kundera missing" at "http://kundera.googlecode.com/svn/maven2/maven-missing-resources"
//resolvers += "Scale 7" at "https://github.com/s7/mvnrepo/raw/master"
