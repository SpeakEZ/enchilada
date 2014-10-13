package com.middil.models

sealed trait Role
case object Admin extends Role
case object Teacher extends Role
case object Student extends Role

case class UserCreationInfo(firstName: String,
                             lastName: String,
                             userName: String,
                             email: String,
                             role: Role)

//case class Grade(activityName: String, grade: Float)
//case class Grades(courseName: String, grades: List[Grade])