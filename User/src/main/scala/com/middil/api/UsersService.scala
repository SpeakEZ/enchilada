package com.middil.api

import akka.actor.{Props, ActorLogging}
import akka.persistence.PersistentActor
import com.middil.models._
import com.middil.user.core.{OneForOneStrategyFactory, IsolatedResumeSupervisor}

object UsersService {
  // Commands ('do this')
  case class CreateUser(info: UserCreationInfo)
  //case class UpdateUser(userName: String, info: )
  case class DeleteUser(userName: String)

  // Events ('did this')
  case class CreatedUser(info: UserCreationInfo)
  //case object UpdatedUser
  case class DeletedUser(userName: String)
}

class UsersService extends PersistentActor with ActorLogging {
  import UsersService._

  override val persistenceId = s"usersService"

  val adminSupervisor = context.actorOf(Props(
    new IsolatedResumeSupervisor with OneForOneStrategyFactory {
      def childStarter(): Unit = {}
    }), "Admins")

  val teacherSupervisor = context.actorOf(Props(
    new IsolatedResumeSupervisor with OneForOneStrategyFactory {
      def childStarter(): Unit = {}
    }), "Teachers")

  val studentSupervisor = context.actorOf(Props(
    new IsolatedResumeSupervisor with OneForOneStrategyFactory {
      def childStarter(): Unit = {}
    }
  ))

  def createUser(msg: CreatedUser): Unit = {
    msg.info.role match {
      case Admin => adminSupervisor ! CreateUser(msg.info)
      case Teacher => teacherSupervisor ! CreateUser(msg.info)
      case Student => studentSupervisor ! CreateUser(msg.info)
    }
  }

  def deleteUser(msg: DeletedUser): Unit = {
    // How to know where the user exists...
    adminSupervisor ! DeleteUser(msg.userName)
    teacherSupervisor ! DeleteUser(msg.userName)
    studentSupervisor ! DeleteUser(msg.userName)
  }

  override def receiveRecover: Receive = {
    case e: CreatedUser => createUser(e)
    case e: DeletedUser => deleteUser(e)
  }

  override def receiveCommand = {
    case CreateUser(info) => {
      createUser(CreatedUser(info))
      persist(CreatedUser(info))(createUser)
    }
    case DeleteUser(userName) => {
      deleteUser(DeletedUser(userName))
      persist(DeletedUser(userName))(deleteUser)
    }
  }
}