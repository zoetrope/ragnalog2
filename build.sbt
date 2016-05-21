name := "ragnalog2"

lazy val baseSettings = Seq(
  organization := "com.arielnetworks.ragnalog",
  version := "2.0.0-SNAPSHOT",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint"
    , "-Xfatal-warnings"
  ),
  resolvers += "embulk-bintray" at "http://dl.bintray.com/embulk/maven/"
)

val akkaVersion = "2.4.6"

lazy val libraries = Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang" % "scala-compiler" % "2.11.8",

  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "ch.megard" %% "akka-http-cors" % "0.1.2",

  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.3.0",
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % "2.3.0",

  "org.antlr" % "ST4" % "4.0.8",

  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",

  "org.apache.commons" % "commons-compress" % "1.11",

  "io.reactivex" %% "rxscala" % "0.26.1",

  "ch.qos.logback" % "logback-classic" % "1.1.7",

  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test"
)

lazy val root = (project in file("."))
  .aggregate(master, node, common)
  .settings(
    baseSettings
  )

lazy val master = (project in file("ragnalog-master"))
  .aggregate(common)
  .dependsOn(common)
  .settings(baseSettings: _*)
  .settings(
    libraryDependencies ++= libraries
  )

lazy val node = (project in file("ragnalog-node"))
  .aggregate(common)
  .dependsOn(common)
  .settings(baseSettings: _*)
  .settings(
    libraryDependencies ++= libraries
  )

lazy val common = (project in file("ragnalog-common"))
  .settings(baseSettings: _*)
  .settings(
    libraryDependencies ++= libraries
  )

