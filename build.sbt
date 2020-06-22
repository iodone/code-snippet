name := "code-snippet"

version := "1.0"

scalaVersion := "2.13.2"

val AkkaVersion = "2.5.31"

// for scala
libraryDependencies ++= Seq(


  // for config
  "com.github.pureconfig" %% "pureconfig" % "0.12.3",

  // for logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

  // alphakka
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "2.0.1",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,

  // for taosdb
  "com.taosdata.jdbc" % "taos-jdbcdriver" % "1.0.3",

  // DB pool
  "com.zaxxer" % "HikariCP" % "3.4.1"
)