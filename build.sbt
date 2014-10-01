import Dependencies._


scalaVersion in ThisBuild := "2.11.4"


lazy val course = (project in file("course")).
//  settings(Commons.settings: _*).
  settings(
    libraryDependencies ++= serviceDependencies
  )
