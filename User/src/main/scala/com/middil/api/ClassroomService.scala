package com.middil.api

import java.util.UUID

import akka.actor.SupervisorStrategy.{Resume, Escalate}
import akka.pattern.ask
import akka.actor._
import com.middil.core.classroom.{Classroom, ClassroomProvider}

object ClassroomService {
  case class CreateClassroom(name: String, courseId: Int)
  case class ProvideClassroom(classroom: ActorRef)
}

class ClassroomService extends Actor {
  import ClassroomService._

  val classroomSupervisor = context.actorOf(Props(
    new ClassroomSupervisor with ClassroomProvider))

  def receive = {
    case m: CreateClassroom =>
      classroomSupervisor ? m onSuccess { case e => sender ! e }
  }
}

class ClassroomSupervisor extends Actor {
  this: ClassroomProvider =>
  import ClassroomService._

  override val supervisorStrategy = OneForOneStrategy() {
    case _: ActorKilledException => Escalate
    case _: ActorInitializationException => Escalate
    case _ => Resume  // Or restart?
  }

  def receive = {
    case CreateClassroom(name, courseId) => {
      val id = UUID.randomUUID
      sender ! ProvideClassroom(context.actorOf(Props(
        newClassroom(name, courseId, id)), s"classroom-$id"))
    }
  }
}