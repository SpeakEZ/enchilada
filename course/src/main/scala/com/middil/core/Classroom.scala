package com.middil.core

import akka.actor.Actor
import scala.collection.mutable

object Classroom {
  case class AddStudent(student: User)
  case class DropStudent(student: User)
  case class TakeCourse(student: User) // Show the first activity if student in students
  case object SuccessResult
  case object FailResult
}

class Classroom(className: String, course: Course) extends Actor {
  import Classroom._

  var students: mutable.Set[User] = mutable.Set.empty[User]

  def receive = {
    case AddStudent(student) => {
      students += student
      sender ! Right(SuccessResult) }
    case DropStudent(student) => {
      students -= student
      sender ! Right(SuccessResult) }
    case TakeCourse(student) => if (students contains student)
      Right(course.activities.head.XML)
      else sender ! Left(FailResult)
  }
}
