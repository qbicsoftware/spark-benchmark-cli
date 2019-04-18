package io.cli
import picocli.CommandLine


class CommandLineParser {

  def parseCommandLineParameters(args: Array[String]): CommandLineOptions = {
    if (args.isEmpty) {
      CommandLine.usage(new CommandLineOptions, System.out)
      System.exit(0)
    }

    val commandLineOptions = new CommandLineOptions()
    new CommandLine(commandLineOptions).parse(args:_*)

    if (commandLineOptions.helpRequested) {
      CommandLine.usage(new CommandLineOptions, System.out)
      System.exit(0)
    }

    commandLineOptions
  }

}
