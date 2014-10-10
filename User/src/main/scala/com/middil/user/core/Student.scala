package com.middil.user.core

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import akka.pattern.ask
import akka.util.Timeout
import com.middil.dataobjects.Grades
import scala.concurrent.Future
import scala.concurrent.duration._

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
  case class Drop(classroom: ActorRef)

  case class ShowGradesForClassroom(classroom: ActorRef)
  case class SetActiveEnrollment(classroom: ActorRef)

  case object AttendClass
  case object GetGradeInfo
}

class Student(userInfo: BaseUserInfo) extends Actor
                                      with ActorLogging {
  this: EnrollmentProvider =>
  import Student._

  implicit val askTimeout = Timeout(1.second)
  implicit val ec = context.dispatcher

  val BaseUserInfo(firstName, lastName, userName, email) = userInfo

  def noEnrollments: Receive = {
    case ShowAllUserInfo =>
      sender ! UserInfo(userInfo, Nil)
    case Enroll(classroom) =>
      context become withEnrollments(context.actorOf(Props(
        newEnrollment(classroom)), s"Enrollment_${classroom.path.name}"))
  }

  def withEnrollments(activeEnrollment: ActorRef): Receive = {
    case ShowAllUserInfo =>
      Future.sequence(
        context.children map { _ ? GetGradeInfo }
      ).mapTo[Iterable[Grades]] onSuccess {
        case grades =>
          sender ! UserInfo(userInfo, grades.toList)
      }

    case SetActiveEnrollment(classroom) =>
      context.child(s"Enrollment_${classroom.path.name}") match {
        case Some(enrollment) =>
          context become withEnrollments(enrollment)
        case None =>
          log info s"$self cannot find enrollment ${classroom.path.name}"
      }

    case Enroll(classroom) =>
      context become withEnrollments(context.actorOf(Props(
        newEnrollment(classroom)), s"Enrollment_$classroom"))

    case Drop(classroom) =>
      context.child(s"Enrollment_${classroom.path.name}") match {
        case Some(enrollment) if enrollment == activeEnrollment =>
          context stop enrollment
          // Do I need to make sure 'stop' has completed first?
          context.children.toSeq match {
            case Seq(nextEnrollment, _*) =>
              context become withEnrollments(nextEnrollment)
            case Nil =>
              context become noEnrollments
          }
        case Some(enrollment) =>
          context stop enrollment
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

    case ShowGradesForClassroom(classroom) =>
      context.child(s"Enrollment_${classroom.path.name}") match {
        case Some(enrollment) =>
          (enrollment ? GetGradeInfo).mapTo[Iterable[Grades]] onSuccess {
            case grades =>
              sender ! UserInfo(userInfo, grades.toList)
          }
        case None =>
          sender ! UserInfo(userInfo, Nil)
      }
  }

  def receive = noEnrollments
}