package com.middil.objects

import java.util.UUID

case class Score(id: UUID, activityId: UUID, userId: UUID, classroomId: UUID,
                 points: Float, possible: Int, grade: Float)

