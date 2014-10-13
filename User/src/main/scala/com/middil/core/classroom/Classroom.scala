package com.middil.core.classroom

import java.util.UUID

import akka.persistence.PersistentActor

trait ClassroomProvider {
  def newClassroom(name: String, courseId: Int, id: UUID): PersistentActor =
    new Classroom(name, courseId, id)
}

object Classroom {

}

class Classroom(name: String, courseId: Int, id: UUID) extends PersistentActor {

}