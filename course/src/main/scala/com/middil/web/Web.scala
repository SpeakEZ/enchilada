package com.middil.web

import com.middil.core.{CoreActors, Core}
import com.middil.api.Api
import akka.io.IO
import spray.can.Http

trait Web {
  this: Api with CoreActors with Core =>

  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
}
