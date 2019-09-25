name := "scark-cli"

version := "1.1.0"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scalactic" %% "scalactic" % "3.0.8",
  "info.picocli" % "picocli" % "4.0.4",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.4.3",
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4",
  "org.apache.spark" %% "spark-mllib" % "2.4.4",
  "org.apache.spark" %% "spark-streaming" % "2.4.4",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)

// see: https://github.com/sbt/sbt-assembly#merge-strategy
assemblyMergeStrategy in assembly := {
  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat // workaround to resolve building issues with the jdbc connector
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
