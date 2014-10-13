package com.middil.core.user

import java.util.UUID

import akka.actor.ActorLogging
import com.middil.core.user.Admin
import com.middil.models.UserCreationInfo

trait AdminProvider extends UserProvider {
  def newUser(info: UserCreationInfo, id: UUID) = new Admin(info, id)
}

object Admin {

}

class Admin(info: UserCreationInfo, id: UUID) extends User
                                                      with ActorLogging {


}