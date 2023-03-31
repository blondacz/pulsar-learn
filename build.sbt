ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val pulsar4sVersion = "2.9.0"
libraryDependencies ++= Seq(
  "com.clever-cloud.pulsar4s" %% "pulsar4s-core" % pulsar4sVersion,

  // if you want to use avro for schemas
  "com.clever-cloud.pulsar4s" %% "pulsar4s-avro" % pulsar4sVersion,

  // if you want to use circe for schemas
  "com.clever-cloud.pulsar4s" %% "pulsar4s-circe" % pulsar4sVersion,

  // if you want to use cats effects
  "com.clever-cloud.pulsar4s" %% "pulsar4s-cats-effect" % pulsar4sVersion,

  // if you want to use fs2
  "com.clever-cloud.pulsar4s" %% "pulsar4s-fs2" % pulsar4sVersion,
  "dev.profunktor" %% "neutron-core" % "0.7.2",
  "dev.profunktor" %% "neutron-circe" % "0.7.2",
  "dev.profunktor" %% "neutron-function" % "0.7.2"

)

lazy val root = (project in file("."))
  .settings(
    name := "pulsar-learn"
  )
