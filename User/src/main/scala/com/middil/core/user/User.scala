package com.middil.core.user

import java.util.UUID

import akka.actor.{Actor, ActorRef}
import akka.persistence.PersistentActor
import com.middil.models.UserCreationInfo

trait UserProvider {
  def newUser(info: UserCreationInfo, id: UUID): Actor
}

object User {

}

trait User extends PersistentActor {
  import User._

}