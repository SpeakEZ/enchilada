package com.middil.api

import akka.actor.ActorRef
import akka.util.Timeout
import com.middil.core.Accounts
import spray.routing.Directives
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.pattern.ask
import spray.http._

class AccountService(accounts: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import Accounts._

  import com.middil.core.MyJsonProtocol._
  implicit val createUserFormat = jsonFormat1(CreateUser)
  implicit val deleteUserFormat = jsonFormat1(DeleteUser)
  implicit val successResultFormat = jsonObjectFormat[SuccessResult.type]
  implicit val failResultFormat = jsonObjectFormat[FailResult.type]

  implicit object EitherErrorSelector extends ErrorSelector[FailResult.type] {
    def apply(f: FailResult.type): StatusCode = StatusCodes.BadRequest
  }


  implicit val timeout = Timeout(2.seconds)

  val route =
    path("createUser") {
      post {
        handleWith { msg: CreateUser =>
          (accounts ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
      }
    } ~
  path("deleteUser") {
    post {
      handleWith { msg: DeleteUser =>
        (accounts ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
    }
  }
}