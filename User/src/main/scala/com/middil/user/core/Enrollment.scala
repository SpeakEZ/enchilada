package com.middil.user.core

import akka.actor.{ActorRef, ActorLogging, Actor}
import com.middil.dataobjects.{Grade, Grades}

trait EnrollmentProvider {
  def newEnrollment(classroom: ActorRef): Actor = new Enrollment(classroom)
}

object Enrollment {
  case class GradeInfo(grades: Grades)
}

class Enrollment(classroom: ActorRef) extends Actor with ActorLogging {
  import Enrollment._

  val grade1 = Grade("Activity 1", 1f)
  val grade2 = Grade("Activity 2", 0.5f)
  val grade3 = Grade("Final Exam", 0f)
  val grades = Grades("Demo Class", List(grade1, grade2, grade3))

  def receive = {
    case Student.GetGradeInfo =>
      sender ! GradeInfo(grades)

    case m: Any =>
      log info s"$self received $m"
  }
}