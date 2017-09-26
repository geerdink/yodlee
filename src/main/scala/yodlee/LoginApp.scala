package yodlee

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import org.json4s._
import org.json4s.jackson.JsonMethods._
import yodlee.domain.{CobrandContext, UserContext}

import scala.concurrent.Future
import scala.util.{Success, Failure}

object LoginApp extends App {
  println("LoginApp START")

  private val fqcn = LoginApp.getClass.getName

  val config = ConfigFactory.load()

  val localURLVer1 = config.getString("yodlee.localURLVer1")
  val coBrandUserName = config.getString("yodlee.coBrandUserName")
  val coBrandPassword = config.getString("yodlee.coBrandPassword")
  val userName = config.getString("yodlee.userName")
  //val userPassword = config.getString("yodlee.userPassword")
  println("Enter Yodlee password:")
  val userPassword = scala.io.StdIn.readLine()

  //  val registerParam = config.getString("yodlee.registerParam")

  val loginTokens = new HashMap[String, String]
  val cobTokens = new HashMap[String, String]

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val formats = DefaultFormats

  val res = for {
    _ <- doCoBrandLogin(coBrandUserName, coBrandPassword)
    y <- doMemberLogin(userName, userPassword)
  } yield y

  //val x = doCoBrandLogin(coBrandUserName, coBrandPassword)
  //val y = x.onComplete(_ => doMemberLogin(userName, userPassword))

  res.onComplete {
    case Success(_) => println("LOGGED IN")
    case Failure(ex) => println("FAILURE: " + ex.getMessage)
    case _ => println("Unknown error")
  }

  def doCoBrandLogin(coBrandUserName: String, coBrandPassword: String): Future[Unit] = {

    def SetCobrand(json: String): Future[Unit] = {
      println("SetCoBrand")
      val cobrand = parse(json).extract[CobrandContext]
      println("COB SESSION = " + cobrand.session.cobSession)
      cobTokens.put("cobSession", cobrand.session.cobSession)
      loginTokens.put("cobSession", cobrand.session.cobSession)
      Future.unit
    }

    println("doCoBrandLogin")

    val mn = "doCoBrandLogin(coBrandUserName " + coBrandUserName + ", coBrandPassword " + coBrandPassword + " )"
    println(fqcn + " :: " + mn)
    //final String requestBody="cobrandLogin="+coBrandUserName+"&cobrandPassword="+coBrandPassword;
    val requestBody = "{" + "\"cobrand\":      {" + "\"cobrandLogin\": " + "\"" + coBrandUserName + "\"" + "," + "\"cobrandPassword\": " + "\"" + coBrandPassword + "\"" + "," + "\"locale\": \"en_US\"" + "}" + "}"
    val coBrandLoginURL = localURLVer1 + "cobrand/login"

    val content = for {
      request <- Marshal(requestBody).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = coBrandLoginURL, entity = request))
      entity <- Unmarshal(response.entity).to[String]
    } yield entity

    content.flatMap(s => SetCobrand(s))
    //Future { content.onComplete(s => SetCobrand(s.get)) }
  }

  def doMemberLogin(userName: String, password: String): Future[Unit] = {
    def SetMember(json: String): Future[Unit] = {
      println("SetMember")

      val member = parse(json).extract[UserContext]
      println("MEMBER SESSION = " + member.session.userSession)
      loginTokens.put("userSession", member.session.userSession)

      Future.unit
    }

    println("doMemberLogin")

    val mn = "doMemberLogin(userLogin=" + userName + ", userPassword=" + userPassword + ", coBrandSessionCredential=" + loginTokens.get("cobSession") + " )"
    println(fqcn + " :: " + mn)

    //final String requestBody="coBrandSessionCredential="+ loginTokens.get("cobSession")+"&loginName=" + userName + "&password="+ userPassword;
    val userLoginURL = localURLVer1 + "user/login"
    val requestBody = "{" + "\"user\":      {" + "\"loginName\": " + "\"" + userName + "\"" + "," + "\"password\": " + "\"" + userPassword + "\"" + "," + "\"locale\": \"en_US\"" + "}" + "}"

    val xx = io.Http.doPostUser(userLoginURL, java.util.HashMap[String, String](), loginTokens.toMap, requestBody, true)

    val header = RawHeader("cobSession", loginTokens("cobSession"))

    val content = for {
      request <- Marshal(requestBody).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = userLoginURL, entity = request).addHeader(header))  // TODO: SESSIONTOKENS loginTokens
        entity <- Unmarshal(response.entity).to[String]
    } yield entity

    content.flatMap(s => SetMember(s))
  }
}
