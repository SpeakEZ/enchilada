package com.middil.core

import java.util.UUID

import akka.actor.Actor
import scala.collection.mutable

object Accounts {
  case class CreateUser(user: MakeUserInfo)
  case class DeleteUser(email: String)
  case object SuccessResult
  case object FailResult
}

class Accounts extends Actor {
  import Accounts._

  var users: mutable.Set[User] = mutable.Set.empty[User]

  def receive = {
    case CreateUser(user) =>
      if (validUserInfo(user)) {
        users += User(UUID.randomUUID,
          user.firstName, user.lastName, user.email, List(), List())
        sender ! Right(SuccessResult) }
      else
        sender ! Left(FailResult)
    case DeleteUser(email) =>
      if (!email.isEmpty) {
        users = users filter (_.email != email)
        sender ! Right(SuccessResult) }
      else
        sender ! Left(FailResult)

  }

  private def validUserInfo(user: MakeUserInfo): Boolean =
    !(user.firstName.isEmpty || user.lastName.isEmpty || user.email.isEmpty)
}