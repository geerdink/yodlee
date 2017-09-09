package yodlee

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest, RequestEntity}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

object LoginApp extends App {
  println("LoginApp START")

  var MAX_LOGINS = 1

  private val fqcn = LoginApp.getClass.getName

  val config = ConfigFactory.load()

  val localURLVer1 = config.getString("yodlee.localURLVer1")
  val coBrandUserName = config.getString("yodlee.coBrandUserName")
  val coBrandPassword = config.getString("yodlee.coBrandPassword")
  val userName = config.getString("yodlee.userName")
  val userPassword = config.getString("yodlee.userPassword")
//  val registerParam = config.getString("yodlee.registerParam")

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def doCoBrandLogin(coBrandUserName: String, coBrandPassword: String): Unit = {
    val mn = "doCoBrandLogin(coBrandUserName " + coBrandUserName + ", coBrandPassword " + coBrandPassword + " )"
    println(fqcn + " :: " + mn)
    //final String requestBody="cobrandLogin="+coBrandUserName+"&cobrandPassword="+coBrandPassword;
    val requestBody = "{" + "\"cobrand\":      {" + "\"cobrandLogin\": " + "\"" + coBrandUserName + "\"" + "," + "\"cobrandPassword\": " + "\"" + coBrandPassword + "\"" + "," + "\"locale\": \"en_US\"" + "}" + "}"
    val coBrandLoginURL = localURLVer1 + "cobrand/login"
    //HTTP.createConnection(coBrandLoginURL);

    val content = for {
      request <- Marshal(requestBody).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri=coBrandLoginURL, entity = request))
      entity <- Unmarshal(response.entity).to[String]
    } yield entity

    content.onComplete(println(_))


   // val jsonResponse = HTTP.doPost(coBrandLoginURL, requestBody)
   // val coBrand = GSONParser.handleJson(jsonResponse, classOf[CobrandContext]).asInstanceOf[CobrandContext]
    // Change the toString() method of the class to decide what to display on the Console.
   // System.out.println(coBrand.toString)
   // cobTokens.put("cobSession", coBrand.getSession.getCobSession)
  //  loginTokens.put("cobSession", coBrand.getSession.getCobSession)
  }

  doCoBrandLogin(coBrandUserName, coBrandPassword)
}
