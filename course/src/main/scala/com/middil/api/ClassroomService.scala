package com.middil.api

import java.util.UUID

import akka.actor.ActorRef
import akka.util.Timeout
import com.middil.core.Classroom
import spray.http.{StatusCodes, StatusCode}
import spray.json._
import spray.routing.Directives
import akka.pattern.ask

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import com.middil.core.MyJsonProtocol._

class ClassroomService(classroom: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import Classroom._

  implicit val addStudentFormat = jsonFormat1(AddStudent)
  implicit val dropStudentFormat = jsonFormat1(DropStudent)
  implicit val takeCourseFormat = jsonFormat1(TakeCourse)
  implicit val showAllStudentsFormat = jsonObjectFormat[ShowAllStudents.type]
  implicit val successResultFormat = jsonObjectFormat[SuccessResult.type]
  implicit val failResultFormat = jsonObjectFormat[FailResult.type]

  implicit object EitherErrorSelector extends ErrorSelector[FailResult.type] {
    def apply(f: FailResult.type): StatusCode = StatusCodes.BadRequest
  }

  implicit val timeout = Timeout(2.seconds)

  val route =
    path("addStudent") {
      post {
        handleWith { msg: AddStudent =>
          (classroom ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
      }
    } ~
  path("dropStudent") {
    post {
      handleWith { msg: DropStudent =>
        (classroom ? msg).mapTo[Either[FailResult.type, SuccessResult.type]] }
    }
  } ~
  path("takeCourse") {
    post {
      handleWith { msg: TakeCourse =>
        (classroom ? msg).mapTo[Either[FailResult.type, String]] }
    }
  } ~
  path("showAllStudents") {
    get {
      complete((classroom ? ShowAllStudents).mapTo[List[UUID]])
    }
  }
}