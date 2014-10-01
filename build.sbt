name := "chocolate-game-example"

description := "Basic game and component interaction examples for the Chocolate game engine"

scalaVersion := "2.11.1"

///////////////////////////////////////////////////////////////////////////////////////////////////

lazy val chocolateGameExamples = FDProject("org.uqbar" %% "chocolate-core" % "[1.0.0-SNAPSHOT)")

///////////////////////////////////////////////////////////////////////////////////////////////////

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)

unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

scalacOptions += "-feature"