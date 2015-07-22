import spray.revolver.RevolverPlugin._

scalaVersion := "2.11.2"


resolvers ++= Seq("Sonatype Releases"   at "http://oss.sonatype.org/content/repositories/releases",
                  "Sonatype OSS"        at "http://oss.sonatype.org/content/repositories/snapshots",
                  "The New Motion Public Repo" at "http://nexus.thenewmotion.com/content/groups/public/",
                  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Spray Repository"    at "http://repo.spray.io/",
                  "Gamlor-Repo"         at "https://raw.github.com/gamlerhart/gamlor-mvn/master/snapshots")


libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "io.spray" %% "spray-json" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-agent" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "org.specs2" %% "specs2-core" % "2.3.11" % "test",
    "com.netaporter.salad" % "salad-metrics-core_2.11" % "0.2.8",
//    "nl.grons" %% "metrics-scala" % "3.3.0_a2.3",
    "org.json4s" %% "json4s-native" % "3.2.10",
    "org.json4s" %% "json4s-jackson" % "3.2.11",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "joda-time" % "joda-time" % "2.7",
    "com.ibm.icu" % "icu4j" % "54.1.1",
    "com.github.scopt" %% "scopt" % "3.3.0"
  )
}

Revolver.settings: Seq[sbt.Def.Setting[_]]

lazy val commonSettings = Seq(
  organization := "com.vegatic",
  licenses := Seq("Vegatic Ltd. All rights reserved" -> url("http://vegatic.com"))
)

lazy val wikimediasearch = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "wikimediasearch",
    version := "0.3.0",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    sbtPlugin := true,
    description := "wikimediasearch REST service to interact with Core modelling tools",
    mainClass in assembly := Some("com.vegatic.wikimediasearch.Boot"),
    assemblyOption in assemblyPackageDependency := (assemblyOption in assemblyPackageDependency).value.copy(appendContentHash = true),
    assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
  )

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter {_.data.getName == "useragent.jar"}
}