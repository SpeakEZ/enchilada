package com.middil.api

import akka.actor.Actor
import com.middil.core.UserInfo

object AccountService {
  case class CreateUser(user: UserInfo)
  case class DeleteUser(email: String)
}

class AccountService extends Actor {
  import AccountService._

  def receive = {
    case CreateUser(user) => null // Generate UUID, save user info somewhere.
    case DeleteUser(email) => null // Find by email, delete said user.
  }
}
