name := "freemoney"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "com.impetus.kundera.client" % "kundera-cassandra" % "3.5"

resolvers += "Kundera" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "Riptano" at "http://mvn.riptano.com/content/repositories/public"
//resolvers += "Kundera missing" at "http://kundera.googlecode.com/svn/maven2/maven-missing-resources"
//resolvers += "Scale 7" at "https://github.com/s7/mvnrepo/raw/master"
