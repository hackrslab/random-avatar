import sbt._
import Keys._

object RandomAvatarBuild extends Build {
  lazy val defaultSettings = Seq(
    organization := "org.hackrslab"
    , name := "random-avatar"
    , version := "0.2.3"
    , autoScalaLibrary := false
    , crossPaths := false
    , javacOptions ++= Seq("-source", "1.6", "-target", "1.6", "-encoding", "UTF-8", "-Xlint:-options")
    , javacOptions in doc := Seq("-source", "1.6")
  )

  lazy val root = Project("root", file("."))
    .settings(defaultSettings: _*)
}
