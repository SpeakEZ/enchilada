import Dependencies._


scalaVersion in ThisBuild := "2.11.2"


lazy val course = (project in file("course")).
//  settings(Commons.settings: _*).
  settings(
    libraryDependencies ++= serviceDependencies
  )
