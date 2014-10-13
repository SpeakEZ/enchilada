import sbt._
import Keys._


object Dependencies {
  val sprayVersion = "1.3.1"
  val akkaVersion = "2.3.6"
  val akkaActor = "com.typesafe.akka"  %% "akka-actor" % akkaVersion
  val akkaSlf4j = "com.typesafe.akka"  %% "akka-slf4j" % akkaVersion
  val akkaTestkit =  "com.typesafe.akka"  %% "akka-testkit"     % akkaVersion  % "test"
  val sprayCan = "io.spray" %% "spray-can" % sprayVersion
  val sprayRouting =  "io.spray" %% "spray-routing" % sprayVersion
  val sprayTestkit = "io.spray" %% "spray-testkit" % sprayVersion % "test"
  val sprayJson = "io.spray" %%  "spray-json" % "1.3.0"
  val logbackClassic =  "ch.qos.logback" % "logback-classic"  % "1.0.13"
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.6"
  val mysql = "mysql" % "mysql-connector-java" % "5.1.26"
  val akkaPersistenceJDBC = "com.github.dnvriend" %% "akka-persistence-jdbc" % "1.0.5"

  val serviceDependencies: Seq[ModuleID] = Seq(
    akkaActor,
    akkaSlf4j,
    akkaTestkit,
    sprayCan,
    sprayRouting,
    sprayTestkit,
    sprayJson,
    logbackClassic,
    akkaPersistence,
    mysql,
    akkaPersistenceJDBC
  )

}
