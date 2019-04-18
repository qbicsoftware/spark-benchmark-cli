name := "spark-benchmark-cli"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "info.picocli" % "picocli" % "3.9.6",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.4.1"
)
