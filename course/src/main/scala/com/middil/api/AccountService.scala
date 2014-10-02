package com.middil.api

import java.util.UUID

import akka.actor.ActorRef
import akka.util.Timeout
import com.middil.core.{User, MakeUserInfo, Accounts}
import spray.routing.Directives
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.pattern.ask
import spray.http._
import com.middil.core.MyJsonProtocol._

class AccountService(accounts: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import Accounts._


  implicit val createUserFormat = jsonFormat1(CreateUser)
  implicit val deleteUserFormat = jsonFormat1(DeleteUser)
  implicit val enrolledStudentFormat = jsonFormat2(EnrolledStudent)
  implicit val droppedStudentFormat = jsonFormat2(DroppedStudent)
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
          (accounts ? msg).mapTo[Either[FailResult.type, String]] }
      } ~
      get { // Make a dummy user
        complete((accounts ? CreateUser(
          MakeUserInfo("Jake", "Stanley", "js@email.com"))).
          mapTo[Either[FailResult.type, String]])
      }
    } ~
  path("deleteUser") {
    post {
      handleWith { msg: DeleteUser =>
        (accounts ? msg).mapTo[Either[FailResult.type, String]] }
    }
  } ~
  path("showAllUsers") {
    get {
      complete((accounts ? ShowAllUsers).mapTo[Either[FailResult.type, List[User]]])
    }
  }
}