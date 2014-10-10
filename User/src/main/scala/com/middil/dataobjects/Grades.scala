package com.middil.dataobjects

case class Grade(activityName: String, grade: Float)
case class Grades(courseName: String, grades: List[Grade])