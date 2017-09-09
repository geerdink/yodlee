import com.typesafe.config.ConfigFactory

import scala.io.Source

object ApiTester extends App {

  println("Hello world!")

  val config = Source.fromResource("config.properties")
  config.getLines().foreach(println(_))

  val config2 = ConfigFactory.load()
  println(config2.getString("test.key1"))
}
