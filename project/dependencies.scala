import sbt._
import Keys._


object Dependencies {
  val sprayVersion = "1.3.0"
  val sprayCan = "io.spray" %% "spray-can" % sprayVersion
  val sprayRouting =  "io.spray" %% "spray-routing" % sprayVersion
  val sprayTestkit = "io.spray" %% "spray-testkit" % sprayVersion % "test"
  val sprayJson = "io.spray" %%  "spray-json" % sprayVersion

  val serviceDependencies: Seq[ModuleID] = Seq(
    sprayCan,
    sprayRouting,
    sprayTestkit,
    sprayJson
  )

}
