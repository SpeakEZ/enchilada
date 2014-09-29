package com.middil.core

import akka.actor.{Props, ActorSystem}


trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  sys.addShutdownHook(system.shutdown())
}

trait CoreActors {
  this: Core =>

  val demo = system.actorOf(Props[DemoActor])
}
