import io.cli.CommandLineParser
import io.parser.PropertiesParser
import java.sql.DriverManager

import org.apache.spark.sql.{DataFrame, SparkSession}
import java.util.Properties

import com.typesafe.scalalogging.Logger
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  val LOG = Logger("Spark Benchmark CLI")

  def main(args: Array[String]) {
    // parse commandline parameters, get database properties
    val commandLineParser = new CommandLineParser()
    val commandLineParameters = commandLineParser.parseCommandLineParameters(args)
    val databaseProperties = PropertiesParser.readPropertiesFile(commandLineParameters.configFilePath)

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
        SparkSession
          .builder()
          .appName("Spark Benchmark CLI")
          .config("spark.master", "local")
          .config("spark.driver.extraClassPath", "/opt/spark-apps/spark-apps/mariadb-java-client-2.4.1.jar")
          .getOrCreate()
      } else {
        SparkSession
          .builder()
          .appName("Spark Benchmark CLI")
          //      .config("spark.some.config.option", "some-value")
          .getOrCreate()
      }
      // For implicit conversions like converting RDDs to DataFrames
      import spark.implicits._

      // connect
      Class.forName("org.mariadb.jdbc.Driver")
      val connection = DriverManager.getConnection(databaseProperties.jdbcURL, databaseProperties.user, databaseProperties.password)
      connection.isClosed

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
      // CreateOrReplaceTempView will create a temporary view of the table in memory. It is not persistent at this moment but you can run sql query on top of that.
      // If you want to save it you can either persist or use saveAsTable to save.
      table.createOrReplaceTempView(commandLineParameters.table)

      val result = spark.sql(commandLineParameters.sqlQuery)
      result.show()
    } else {
      // RUNNING OUTSIDE SPARK
      LOG.info("Spark support has been disabled!")

      // connect
      Class.forName("org.mariadb.jdbc.Driver")
      val connection = DriverManager.getConnection(databaseProperties.jdbcURL, databaseProperties.user, databaseProperties.password)
      connection.isClosed

      // execute query
      val statement = connection.createStatement()
      val rs = statement.executeQuery(commandLineParameters.sqlQuery)

      // print results
      val list = Iterator.from(0).takeWhile(_ => rs.next()).map(_ => rs.getString(1)).toList
      println(list)

//      while (rs.next()) {
//        println(rs.getString(1)) //or rs.getString("column name");
//      }

      connection.isClosed
    }

  }
}
