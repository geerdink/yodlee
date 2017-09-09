package yodlee

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s._
import org.json4s.jackson.JsonMethods._
import yodlee.domain.CobrandContext

object LoginApp extends App {
  println("LoginApp START")

  private val fqcn = LoginApp.getClass.getName

  val config = ConfigFactory.load()

  val localURLVer1 = config.getString("yodlee.localURLVer1")
  val coBrandUserName = config.getString("yodlee.coBrandUserName")
  val coBrandPassword = config.getString("yodlee.coBrandPassword")
  val userName = config.getString("yodlee.userName")
  val userPassword = config.getString("yodlee.userPassword")
//  val registerParam = config.getString("yodlee.registerParam")

  val loginTokens = new HashMap[String, String]
  val cobTokens = new HashMap[String, String]

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val formats = DefaultFormats

  doCoBrandLogin(coBrandUserName, coBrandPassword)

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

    content.onComplete(s => SetCobrand(s.get))

     def SetCobrand(json: String) = {
     //  println(json)
       val cobrand = parse(json).extract[CobrandContext]

       println("COB SESSION = " + cobrand.session.cobSession)
       cobTokens.put("cobSession", cobrand.session.cobSession)
       loginTokens.put("cobSession", cobrand.session.cobSession)
     }
  }


  def doMemberLogin(userName: String, password: String) = {
    // TODO

  }
}
