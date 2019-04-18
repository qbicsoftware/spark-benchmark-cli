import io.cli.CommandLineParser
import io.parser.PropertiesParser
import java.sql.DriverManager


object Main {

  def main(args: Array[String]) {
    // parse commandline parameters, get database properties
    val commandLineParser = new CommandLineParser()
    val commandLineParameters = commandLineParser.parseCommandLineParameters(args)
    val databaseProperties = PropertiesParser.readPropertiesFile(commandLineParameters.configFilePath)

    // connect
    Class.forName("org.mariadb.jdbc.Driver")
    val connection = DriverManager.getConnection(databaseProperties.jdbcURL, databaseProperties.user, databaseProperties.password)

    // execute query
    val statement = connection.createStatement()
    val rs = statement.executeQuery(commandLineParameters.sqlQuery)

    // print results
    val list = Iterator.from(0).takeWhile(_ => rs.next()).map(_ => rs.getString(1)).toList
    println(list)

    while(rs.next())
    {
      println(rs.getString(1)) //or rs.getString("column name");
    }

    connection.isClosed
  }
}
