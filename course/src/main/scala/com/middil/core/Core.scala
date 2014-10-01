package com.middil.core

import akka.actor.{Props, ActorSystem}
import java.util.UUID

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit lazy val system = ActorSystem("akka-spray")

  sys.addShutdownHook(system.shutdown())
}

trait CoreActors {
  this: Core =>

//  val accounts = system.actorOf(Props[Accounts])
//
//  val demoActivity1 = Activity(UUID.randomUUID, "Activity 1", "<h1>Activity 1</h1>")
//  val demoActivity2 = Activity(UUID.randomUUID, "Activity 2", "<h1>Activity 2</h1>")
//  val demoCourse = Course(UUID.randomUUID, "Demo Course", List(demoActivity1, demoActivity2))
//
//  val classroom = system.actorOf(Props(new Classroom("Demo Class", demoCourse)))
}
