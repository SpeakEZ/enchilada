package com.middil.core.user

import java.util.UUID

import akka.actor.SupervisorStrategy.{Resume, Escalate}
import akka.actor._
import com.middil.api.UsersService.{DeleteUser, CreateUser}

object UserSupervisor {
  // Commands

  // Events


}

class UserSupervisor extends Actor {
  this: UserProvider =>
  import UserSupervisor._

  override val supervisorStrategy = OneForOneStrategy() {
    case _: ActorKilledException => Escalate
    case _: ActorInitializationException => Escalate
    case _ => Resume       // Resume or restart?
  }

  def receive = {
    case CreateUser(info) => {
      val id = UUID.randomUUID
      context.actorOf(Props(newUser(info, id)), s"user-$id")
    }

    case DeleteUser(userId) => context.child(s"user-$userId") match {
      case Some(user) => context stop user
      case None =>
    }
  }
}