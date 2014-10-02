package com.middil.core

import java.util.UUID

import akka.actor.{ActorRef, Actor}
import com.middil.core.Accounts.{DroppedStudent, EnrolledStudent}
import scala.collection.mutable

object Classroom {
  case class AddStudent(studentRef: UUID)
  case class DropStudent(studentRef: UUID)
  case class TakeCourse(studentRef: UUID) // Show the first activity if student in students
  case object ShowAllStudents
  case object SuccessResult
  case object FailResult
}

class Classroom(className: String, course: Course, eventSource: ActorRef) extends Actor {
  import Classroom._
  import EventSource._

  override def preStart() { eventSource ! RegisterListener(self) }


  val myID = UUID.randomUUID
  var studentRefs: mutable.Set[UUID] = mutable.Set.empty[UUID]

  def classroomReceive: Receive = {
    case AddStudent(studentRef) => {
      studentRefs += studentRef
      eventSource ! BroadcastMessage(EnrolledStudent(studentRef, myID))
      sender ! Right(SuccessResult) }
    case DropStudent(studentRef) => {
      studentRefs -= studentRef
      eventSource ! BroadcastMessage(DroppedStudent(studentRef, myID))
      sender ! Right(SuccessResult) }
    case TakeCourse(studentRef) =>
      if (studentRefs contains studentRef)
        sender ! Right(course.activities.head.XML)
      else sender ! Left(FailResult)
    case ShowAllStudents => sender ! studentRefs.toList
  }

  def receive = classroomReceive
}
