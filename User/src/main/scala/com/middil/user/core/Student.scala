package com.middil.user.core

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import akka.pattern.ask
import com.middil.dataobjects.Grades

import scala.concurrent.Future

case class BaseUserInfo(firstName: String,
                        lastName: String,
                        userName: String,
                        email: String)

trait StudentProvider {
  def newStudent(info: BaseUserInfo): Actor =
    new Student(info) with EnrollmentProvider
}

object Student {
  case object ShowAllUserInfo
  case class UserInfo(userInfo: BaseUserInfo, grades: List[Grades])
  case object ShowAllEnrollments
  case class AllEnrollments(enrollments: List[ActorRef])

  case class Enroll(classroom: ActorRef)
  case class Drop(classroomName: String)

  case class ShowGradesForClassroom(classroomName: String)
  case class SetActiveEnrollment(classroomName: String)

  case object AttendClass
  case object GetGradeInfo
}

class Student(userInfo: BaseUserInfo) extends Actor
                                  with ActorLogging {
  this: EnrollmentProvider =>
  import Student._

  val BaseUserInfo(firstName, lastName, userName, email) = userInfo

  def noEnrollments: Receive = {
    case ShowAllUserInfo =>
      sender ! UserInfo(userInfo, Nil)
    case Enroll(classroom) =>
      context become withEnrollments(context.actorOf(Props(
        newEnrollment(classroom)), s"Enrollment $classroom"))
  }

  def withEnrollments(activeEnrollment: ActorRef): Receive = {
    case ShowAllUserInfo =>
      Future.sequence(
        context.children map { _ ? GetGradeInfo }
      ).mapTo[Iterable[Grades]] onSuccess {
        case grades =>
          sender ! UserInfo(userInfo, grades.toList)
      }

    case SetActiveEnrollment(classroomName) =>
      context.actorSelection(classroomName).resolveOne() onSuccess {
        case e =>
          context become withEnrollments(e)
      }

    case Enroll(classroom) =>
      context become withEnrollments(context.actorOf(Props(
        newEnrollment(classroom)), s"Enrollment $classroom"))

    case Drop(classroomName) =>
      context.actorSelection(classroomName).resolveOne() onSuccess {
        case e =>
          context stop e
          context.children.toSeq match {
            case Seq(enrollment, _*) =>
              context become withEnrollments(enrollment)
            case Nil =>
              context become noEnrollments
          }
      }

    case AttendClass =>
      activeEnrollment ! AttendClass

    case GetGradeInfo =>
      (activeEnrollment ? GetGradeInfo) onSuccess {
        case Enrollment.GradeInfo(grades) =>
          sender ! UserInfo(userInfo, List(grades))
      }

    case ShowAllEnrollments =>
      sender ! AllEnrollments(context.children.toList)
  }

  def receive = noEnrollments
}