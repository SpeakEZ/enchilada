package com.middil.core

import akka.actor.{Actor, ActorRef}

object EventSource {
  case class RegisterListener(listener: ActorRef)
  case class UnregisterListener(listener: ActorRef)
  case class BroadcastMessage[T](msg: T)
}

class EventSource extends Actor {
  import EventSource._

  var listeners = Vector.empty[ActorRef]

  def sendEvent[T](event: T): Unit = listeners foreach (_ ! event)

  def eventSourceReceive: Receive = {
    case RegisterListener(listener) =>
      listeners = listeners :+ listener
    case UnregisterListener(listener) =>
      listeners = listeners filter (_ != listener)
    case BroadcastMessage(msg) => sendEvent(msg)
  }

  def receive = eventSourceReceive
}