package com.middil.core.user

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.middil.models.{Grade, Grades}

trait EnrollmentProvider {
  def newEnrollment(classroom: ActorRef): Actor = new Enrollment(classroom)
}

object Enrollment {
  case class GradeInfo(grades: Grades)
}

class Enrollment(classroom: ActorRef) extends Actor with ActorLogging {
  import com.middil.core.user.Enrollment._

  val grade1 = Grade("Activity 1", 1f)
  val grade2 = Grade("Activity 2", 0.5f)
  val grade3 = Grade("Final Exam", 0f)
  val grades = Grades("Demo Class", List(grade1, grade2, grade3))

  def receive = {
    case Student.GetGradeInfo =>
      sender ! GradeInfo(grades)

    case m =>
      log info s"$self received $m"
  }
}