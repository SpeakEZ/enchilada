package com.middil.core

import java.util.UUID

case class UserInfo(firstName: String, lastName: String, email: String)
case class User(id: UUID, firstName: String, lastName: String, email: String)

case class Activity(id: UUID, name: String, XML: String)
case class Course(id: UUID, name: String, activityRefs: List[UUID])
case class Class(id: UUID, name: String, courseRef: UUID, studentRefs: List[UUID])

case class Grade(id: UUID, userRef: UUID, classRef: UUID, activityRef: UUID, points: Int, grade: Float)