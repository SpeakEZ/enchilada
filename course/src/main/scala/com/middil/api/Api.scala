package com.middil.api

import com.middil.core.{CoreActors, Core}
import akka.actor.Props
import spray.routing.RouteConcatenation
import spray.routing.Directives._

trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher


  val routes = path("ping") {
    get {
      complete("PONG!")
    }
  }
//  val routes =
//    new AccountService(accounts).route
//    new ClassroomService(classroom).route
    //new DemoService(demo).route


  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))

}
