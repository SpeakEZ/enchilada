package com.middil.core

import akka.actor.Actor

object DemoActor {
  case class Ping(ping: String)
}

class DemoActor extends Actor {
  import DemoActor._

  def receive: Receive = {
    case Ping(ping) => sender ! "pong" // TODO - what if not 'ping' sent?
  }

}
