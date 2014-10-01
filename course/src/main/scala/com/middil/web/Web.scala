package com.middil.web

import akka.util.Timeout
import com.middil.core.{CoreActors, Core}
import com.middil.api.Api
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import scala.concurrent.duration._

trait Web {
  this: Api with CoreActors with Core =>

  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
}
