package com.middil.api

import java.util.UUID

import akka.actor.{Actor, Props, ActorLogging}
import akka.persistence.PersistentActor
import com.middil.core.user._
import com.middil.models.Admin
import com.middil.models.Student
import com.middil.models.Teacher
import com.middil.models._

object UsersService {
  // Commands ('do this')
  case class CreateUser(info: UserCreationInfo)
  //case class UpdateUser(userName: String, info: )
  case class DeleteUser(userId: UUID)

  // Events ('did this')
  case class CreatedUser(info: UserCreationInfo)
  //case object UpdatedUser
  case class DeletedUser(userId: UUID)
}

class UsersService extends Actor with ActorLogging {
  import UsersService._

//  override val persistenceId = s"usersService"

  val adminSupervisor = context.actorOf(Props(
    new UserSupervisor with AdminProvider), "Admins")

  val teacherSupervisor = context.actorOf(Props(
    new UserSupervisor with TeacherProvider), "Teachers")

  val studentSupervisor = context.actorOf(Props(
    new UserSupervisor with StudentProvider), "Students")

  def receive = {
    case m@CreateUser(info) => info.role match {
      case Admin => adminSupervisor forward m
      case Teacher => teacherSupervisor forward m
      case Student => studentSupervisor forward m
    }
    case m: DeleteUser => {
      // How to know where the user exists...
      adminSupervisor forward m
      teacherSupervisor forward m
      studentSupervisor forward m
    }
  }

//
//  def createUser(msg: CreatedUser): Unit = {
//    msg.info.role match {
//
//    }
//  }
//
//  def deleteUser(msg: DeletedUser): Unit = {
//
//  }
//
//  override def receiveRecover: Receive = {
//    case e: CreatedUser => createUser(e)
//    case e: DeletedUser => deleteUser(e)
//  }
//
//  override def receiveCommand = {
//    case CreateUser(info) => {
//      createUser(CreatedUser(info))
//      persist(CreatedUser(info))(createUser)
//    }
//    case DeleteUser(userName) => {
//      deleteUser(DeletedUser(userName))
//      persist(DeletedUser(userName))(deleteUser)
//    }
//  }
}