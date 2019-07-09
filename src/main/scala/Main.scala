import io.cli.CommandLineParser
import io.parser.PropertiesParser
import java.sql.DriverManager

import org.apache.spark.sql.SparkSession
import java.util.Properties

import com.typesafe.scalalogging.Logger

object Main {

  val LOG = Logger("scark-cli")

  def main(args: Array[String]) {
    LOG.info(
      """
        |  _________                    __                    .__  .__
        | /   _____/ ____ _____ _______|  | __           ____ |  | |__|
        | \_____  \_/ ___\\__  \\_  __ \  |/ /  ______ _/ ___\|  | |  |
        | /        \  \___ / __ \|  | \/    <  /_____/ \  \___|  |_|  |
        |/_______  /\___  >____  /__|  |__|_ \          \___  >____/__|
        |        \/     \/     \/           \/              \/         """.stripMargin)
    // parse commandline parameters, get database properties
    val commandLineParser = new CommandLineParser()
    val commandLineParameters = commandLineParser.parseCommandLineParameters(args)
    val databaseProperties = PropertiesParser.readPropertiesFile(commandLineParameters.configFilePath)

    try {
      if (commandLineParameters.sparkSupport) {
        LOG.info("Enabling Spark support")

        if (databaseProperties.jdbcURL.contains("mariadb")) {
          LOG.warn("________________________________________________________________________________________________")
          LOG.warn("Please note that specifying mariadb in the jdbc URL can lead to corrupt MariaDB access.")
          LOG.warn("Replace mariadb with mysql in your jdbc URL if you are running into issues.")
          LOG.warn("More information can be found here: https://github.com/qbicsoftware/spark-benchmark-cli/issues/9")
          LOG.warn("________________________________________________________________________________________________")
        }

        val spark =
          if (commandLineParameters.localMode) {
            LOG.info("Running Spark in local mode!")
            LOG.warn("scark-cli assumes that your mariadb-java-client jar is in /opt/spark-apps")
            SparkSession
              .builder()
              .appName("Spark Benchmark CLI")
              .config("spark.master", "local")
              .config("spark.driver.extraClassPath", "/opt/spark-apps/mariadb-java-client-2.4.1.jar")
              .getOrCreate()
          } else {
            SparkSession
              .builder()
              .appName("scark-cli")
              //      .config("spark.some.config.option", "some-value")
              .getOrCreate()
          }
        // For implicit conversions like converting RDDs to DataFrames
        import spark.implicits._

        Class.forName("org.mariadb.jdbc.Driver")

        // connect to the database
        try {
          val connection = DriverManager.getConnection(databaseProperties.jdbcURL, databaseProperties.user, databaseProperties.password)
        } catch {
          case e: Exception => LOG.error(s"Unable to connect to the database ${e.getMessage}")
        }

        // Spark likes working with properties, hence we create a properties object
        val connectionProperties = new Properties()
        connectionProperties.put("user", s"${databaseProperties.user}")
        connectionProperties.put("password", s"${databaseProperties.password}")
        connectionProperties.put("driver", s"${commandLineParameters.databaseDriver}")

        val table = spark.read.jdbc(databaseProperties.jdbcURL, commandLineParameters.table, connectionProperties)
        table.printSchema()
        table.show()

        // NOTE
        // Spark requires a View of a table to allow for SQL queries
        // CreateOrReplaceTempView will create a temporary view of the table in memory.
        // It is not persistent at this moment but you can run sql queries on top of that.
        // If you want to save it you can either persist or use saveAsTable to save.
        table.createOrReplaceTempView(commandLineParameters.table)

        val result = spark.sql(commandLineParameters.sqlQuery)
        result.show()
      } else {
        // RUNNING OUTSIDE SPARK
        LOG.info("Spark support has been disabled!")

        // connect
        val connection = DriverManager.getConnection(databaseProperties.jdbcURL, databaseProperties.user, databaseProperties.password)

        // execute query
        val statement = connection.createStatement()
        val rs = statement.executeQuery(commandLineParameters.sqlQuery)

        // print results
        val list = Iterator.from(0).takeWhile(_ => rs.next()).map(_ => rs.getString(1)).toList
        println(list)

        connection.isClosed
      }
    } catch {
      case eie: ExceptionInInitializerError =>
        LOG.error("__________________________________________________________________________________________")
        LOG.error(s"Hadoop support is possibly wrongly configured or missing! ${eie.getException.getMessage}")
        LOG.error("__________________________________________________________________________________________")
      case e: Exception => LOG.error(s"Hadoop support is possibly wrongly configured or missing! ${e.getMessage}")
                           LOG.error("Don't forget to specify the name of the database driver!")
    }

  }
}
