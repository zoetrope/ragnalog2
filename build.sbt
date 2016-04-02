name := "ragnalog2"

version := "2.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "embulk-bintray" at "http://dl.bintray.com/embulk/maven/"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "com.typesafe.akka" %% "akka-http-core" % "2.4.3",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream" % "2.4.3",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.2.1",
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % "2.2.1",
  "io.reactivex" %% "rxscala" % "0.26.0",
  "org.embulk" % "embulk-core" % "0.8.8",
  "org.embulk" % "embulk-standards" % "0.8.8",

  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
