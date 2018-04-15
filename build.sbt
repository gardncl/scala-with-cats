name := "scala-with-cats"

version := "0.1"

scalaVersion := "2.12.5"

scalacOptions += "-Ypartial-unification"
scalacOptions += "-language:higherKinds"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.1.0",
  "com.chuusai" %% "shapeless" % "2.3.2"
)