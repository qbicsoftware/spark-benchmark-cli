import io.cli.CommandLineParser
import io.parser.PropertiesParser
import java.sql.DriverManager
import org.apache.spark.sql.SparkSession
import java.util.Properties
import com.typesafe.scalalogging.Logger

object Main {

  val LOG = Logger("Spark Benchmark CLI")

  def main(args: Array[String]) {
    // parse commandline parameters, get database properties
    val commandLineParser = new CommandLineParser()
    val commandLineParameters = commandLineParser.parseCommandLineParameters(args)
    val databaseProperties = PropertiesParser.readPropertiesFile(commandLineParameters.configFilePath)

    if (commandLineParameters.sparkSupport) {
      LOG.info("Enabling Spark support")

      val spark =
      if (commandLineParameters.localMode) {
        LOG.info("Running Spark in local mode!")
        SparkSession
          .builder()
          .appName("Spark Benchmark CLI")
          .config("spark.master", "local")
          //      .config("spark.some.config.option", "some-value")
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

      val table = spark.read.jdbc(databaseProperties.jdbcURL, commandLineParameters.table, connectionProperties)
      table.printSchema()

      // NOTE
      // Spark requires a View of a table to allow for SQL queries
      // CreateOrReplaceTempView will create a temporary view of the table in memory. It is not persistent at this moment but you can run sql query on top of that.
      // If you want to save it you can either persist or use saveAsTable to save.
      table.createOrReplaceTempView(commandLineParameters.table)

      spark.sql(commandLineParameters.sqlQuery)
    }

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

    while (rs.next()) {
      println(rs.getString(1)) //or rs.getString("column name");
    }

    connection.isClosed
  }
}
