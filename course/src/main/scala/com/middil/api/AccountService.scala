package com.middil.api

import akka.actor.ActorRef
import akka.util.Timeout
import com.middil.core.{User, MakeUserInfo, Accounts}
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.routing.Directives
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.pattern.ask
import spray.http._

case class JustPrintThisString(string: String)

//object JustPrintThisStringJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
//  implicit val PortfolioFormats = jsonFormat1(JustPrintThisString)
//}

class AccountService(accounts: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import Accounts._

  import com.middil.core.MyJsonProtocol._

  implicit val createUserFormat = jsonFormat1(CreateUser)
  implicit val deleteUserFormat = jsonFormat1(DeleteUser)
  implicit val successResultFormat = jsonObjectFormat[SuccessResult.type]
  implicit val failResultFormat = jsonObjectFormat[FailResult.type]

  implicit val justPrintThisStringFormat = jsonFormat1(JustPrintThisString)

  implicit object EitherErrorSelector extends ErrorSelector[FailResult.type] {
    def apply(f: FailResult.type): StatusCode = StatusCodes.BadRequest
  }

  implicit val timeout = Timeout(2.seconds)


  val justPrintThisString = (stringy: JustPrintThisString) => stringy.string

  val route =
    path("createUser") {
      post {
        handleWith { msg: CreateUser =>
          (accounts ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
      } ~
      get { // Make a dummy user - hard to CURL JSON.
        complete((accounts ? CreateUser(
          MakeUserInfo("Jake", "Stanley", "js@email.com"))).
          mapTo[Either[FailResult.type, SuccessResult.type]])
      }
    } ~
  path("deleteUser") {
    post {
      handleWith { msg: DeleteUser =>
        (accounts ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
    }
  } ~
  path("showAllUsers") {
    get {
      complete((accounts ? ShowAllUsers).mapTo[Either[FailResult.type, List[User]]])
    }
  } ~
  path("showmethedamnjson") {
    get {
      complete(CreateUser(MakeUserInfo("tim","meadows","tmeadows@snl.com")))
    }
  } ~
  path("justprintthisstring") {
    post {
      handleWith(justPrintThisString)
    }
  }
}