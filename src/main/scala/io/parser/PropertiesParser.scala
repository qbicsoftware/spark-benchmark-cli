package io.parser

import core.DatabaseProperties

import scala.io.Source

object PropertiesParser {

  def readPropertiesFile(propertiesFileName: String): DatabaseProperties = {
    val lines = Source.fromFile(propertiesFileName).getLines.toList
    val split = for (line <- lines) yield line.split(":")

    var host = split.head(1) + ":" + split.head(2) + ":" + split.head(3)
    var port = split(1)(1)
    var name = split(2)(1)
    var password = split(3)(1)
    var user = split(4)(1)
    val properties = List(host, port, name, password, user)

    // remove any backslashes and whitespaces
    val formatted_properties = properties
        .map(string => string.filterNot((x: Char) => x.equals('\"')))
        .map(string => string.filterNot((x: Char) => x.isWhitespace))
    host = formatted_properties.head
    port = formatted_properties(1)
    name = formatted_properties(2)
    password = formatted_properties(3)
    user = formatted_properties(4)

    DatabaseProperties(host, user, password, port, name)
  }

}