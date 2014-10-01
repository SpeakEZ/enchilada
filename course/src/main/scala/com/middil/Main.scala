package com.middil

import java.util.UUID

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import scala.concurrent.duration._
import com.middil.api.{ClassroomService, AccountService, RoutedHttpService, Api}
import com.middil.core._
import com.middil.web.Web
import akka.pattern.ask
import spray.routing.Directives._

//object Main extends App with Web with Api with BootedCore with CoreActors

// This works
//object Main extends App {
//  implicit val system = ActorSystem("BAH")
//  val route = path("ping") { get { complete("PONG")} }
//  val service = system.actorOf(Props(new RoutedHttpService(route)))
//  implicit val timeout = Timeout(5.seconds)
//  IO(Http)(system) ! Http.Bind(service, "0.0.0.0", port = 8080)
//}

object Main extends App {
  implicit lazy val system = ActorSystem("course")
  sys.addShutdownHook(system.shutdown())

  val accounts = system.actorOf(Props[Accounts])

  val demoActivity1 = Activity(UUID.randomUUID, "Activity 1", "<h1>Activity 1</h1>")
  val demoActivity2 = Activity(UUID.randomUUID, "Activity 2", "<h1>Activity 2</h1>")
  val demoCourse = Course(UUID.randomUUID, "Demo Course", List(demoActivity1, demoActivity2))

  val classroom = system.actorOf(Props(new Classroom("Demo Class", demoCourse)))

  private implicit val _ = system.dispatcher

  val routes =
    new AccountService(accounts).route ~
      new ClassroomService(classroom).route

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))


  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
}