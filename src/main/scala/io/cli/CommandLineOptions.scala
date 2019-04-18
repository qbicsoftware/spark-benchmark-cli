package io.cli

import picocli.CommandLine.{Command, Option}

@Command(name = "Benchmark", version = Array("1.0"),
  description = Array("@|bold Benchmark Tool|@ for evaluating the performance of a Spark Cluster. Run custom SQL Queries inside Spark!"))
class CommandLineOptions {

  @Option(names = Array("-c", "--config"),
    description = Array("database config file path"),
    required = true)
  var configFilePath: String = ""

  @Option(names = Array("-q", "--query"),
    description = Array("SQL query to execute"),
    arity = "0..1",
    interactive = true,
    required = true)
  var sqlQuery: String = ""

  @Option(names = Array("-h", "--help"),
    description = Array("display a help message"),
    usageHelp = true,
  )
  var helpRequested = false
}
