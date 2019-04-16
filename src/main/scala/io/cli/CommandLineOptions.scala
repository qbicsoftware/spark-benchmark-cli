package io.cli

import picocli.CommandLine.{Command, Option}

@Command(name = "Benchmark", version = Array("1.0"),
  description = Array("@|bold Scala|@ @|underline picocli|@ example"))
class CommandLineOptions {

  @Option(names = Array("-c", "--config"), description = Array("database config file path"))
  var configFilePath: String = ""

//  @Option(names = Array("-q", "--query"), description = Array("SQL query to execute"))
//  var sqlQuery: String = ""

  @Option(names = Array("-h", "--help"), usageHelp = true, description = Array("display a help message"))
  var helpRequested = false
}
