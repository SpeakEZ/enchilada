package com.middil.core

import java.util.UUID

import akka.actor.{ActorRef, Actor}
import scala.collection.mutable

object Accounts {
  case class CreateUser(userInfo: MakeUserInfo)
  case class DeleteUser(email: String)
  case class EnrolledStudent(userRef: UUID, classRef: UUID)
  case class DroppedStudent(userRef: UUID, classRef: UUID)
  case object ShowAllUsers
  case object SuccessResult
  case object FailResult
}

class Accounts(eventSource: ActorRef) extends Actor {
  import Accounts._
  import EventSource._

  override def preStart() { eventSource ! RegisterListener(self) }

  var users: mutable.Set[User] = mutable.Set.empty[User]

  def accountsReceive: Receive = {
    case CreateUser(userInfo) =>
      if (userInfo.validUser) {
        users += User(UUID.randomUUID,
                      userInfo.firstName, userInfo.lastName, userInfo.email, List(), List())
        sender ! Right(s"${userInfo.email} has been created.")
      }
      else
        sender ! Left(FailResult)
    case DeleteUser(email) =>
      if (email.isEmpty || !(users exists (_.email == email)))
        sender ! Left(FailResult)
      else {
        users = users filter (_.email != email)
        sender ! Right(s"$email has been deleted.")
      }
    case EnrolledStudent(userRef, classRef) =>
      users find (_.id == userRef) match {
        case Some(u) => {
          val updatedUser = u.copy(classes = classRef :: u.classes)
          users = (users filter (_.id != userRef)) + updatedUser
        }
        case None => Left(FailResult)
      }
    case DroppedStudent(userRef, classRef) =>
      users find (_.id == userRef) match {
        case Some(u) => {
          val updatedUser = u.copy(classes = u.classes filter (_ != classRef))
          users = (users filter (_.id != userRef)) + updatedUser
        }
        case None => Left(FailResult)
      }
    case ShowAllUsers =>
      if (users.isEmpty) sender ! Left(FailResult)
      else sender ! Right(users.toList)
  }

  def receive = accountsReceive
}