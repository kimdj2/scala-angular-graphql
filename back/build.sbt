name := """back"""
organization := "graphql"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.7"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,

  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "mysql" % "mysql-connector-java" % "5.1.29",

  "org.sangria-graphql" %% "sangria" % "2.0.1",
  "org.sangria-graphql" %% "sangria-slowlog" % "2.0.1",
  "org.sangria-graphql" %% "sangria-play-json" % "2.0.1"
)

enablePlugins(FlywayPlugin)

import sbt._
import com.typesafe.config.ConfigFactory

lazy val flywayDBName = "default"
lazy val flywayDbConf = settingKey[(String, String, String, String)]("Typesafe config file with slick settings")
flywayDbConf := {
    val cfg = ConfigFactory.parseFile((resourceDirectory in Compile).value / "application.conf")
    val prefix = s"slick.dbs.${flywayDBName}.db"
    (cfg.getString(s"$prefix.url"), cfg.getString(s"$prefix.user"), cfg.getString(s"$prefix.password"), cfg.getString("flyway.location"))
}

flywayUrl := flywayDbConf.value._1
flywayUser := flywayDbConf.value._2
flywayPassword := flywayDbConf.value._3
flywayLocations += flywayDbConf.value._4