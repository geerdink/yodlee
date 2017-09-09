name := "InstantMortgage"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.typesafe"       % "config"               % "1.3.1",
  "com.typesafe.akka" %% "akka-http"            % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit"    % "10.0.10"   % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
  "org.json4s"        %% "json4s-native"        % "3.5.3",
  "org.json4s"        %% "json4s-jackson"       % "3.5.3"
)
