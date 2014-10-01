package com.middil.core

import java.util.UUID

import spray.json._

object MyJsonProtocol extends DefaultJsonProtocol {
  // This is in DefaultJsonFormats but needed it here too... doing something wrong.
  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)
    def read(value: JsValue) = value match {
        case JsString(x) => UUID.fromString(x)
        case x           => deserializationError("Expected UUID as JsString, but got " + x)
      }
  }
  implicit val userFormat = jsonFormat6(User)
  implicit val makeUserInfoFormat = jsonFormat3(MakeUserInfo)
  implicit val activityFormat = jsonFormat3(Activity)
  implicit val courseFormat = jsonFormat3(Course)
  implicit val gradeFormat = jsonFormat6(Grade)
}

case class User(id: UUID,
                firstName: String,
                lastName: String,
                email: String,
                classes: List[UUID],
                grades: List[UUID])

case class MakeUserInfo(firstName: String, lastName: String, email: String)

case class Activity(id: UUID, name: String, XML: String)
case class Course(id: UUID, name: String, activities: List[Activity])

case class Grade(id: UUID,
                 userRef: UUID,
                 classRef: UUID,
                 activityRef: UUID,
                 points: Int,
                 grade: Float)