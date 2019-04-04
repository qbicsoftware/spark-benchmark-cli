package database.parser

import database.core.DatabaseProperties

import scala.io.Source._

class PropertiesParser {

  def readPropertiesFile(propertiesFileName: String): DatabaseProperties = {
    val lines = fromFile(propertiesFileName).getLines

    // I shall map lines to the split content
    //val splitContent =


    null
  }

}
