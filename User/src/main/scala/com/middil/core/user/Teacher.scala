package com.middil.core.user

import java.util.UUID

import akka.actor.ActorLogging
import com.middil.core.user.Teacher
import com.middil.models.UserCreationInfo

trait TeacherProvider extends UserProvider {
  def newUser(info: UserCreationInfo, id: UUID) = new Teacher(info, id)
}

object Teacher {

}

class Teacher(info: UserCreationInfo, id: UUID) extends User
                                                        with ActorLogging {
  import Teacher._
}