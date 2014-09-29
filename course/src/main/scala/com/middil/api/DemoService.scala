package com.middil.api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import com.middil.core.DemoActor


class DemoService(demo: ActorRef)(implicit executionContext: ExecutionContext) extends Directives with DefaultJsonFormats {
  import DemoActor._

  val route =
    path("demo") {
      get {

      }
    }

}
