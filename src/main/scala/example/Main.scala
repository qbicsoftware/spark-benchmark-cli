package example

import database.parser.PropertiesParser

object Main extends App {
  val ages = Seq(42, 75, 29, 64)
  println(s"The oldest person is ${ages.max}")

  val propertiesParser = new PropertiesParser()
  propertiesParser.readPropertiesFile("SomeFile.txt")



//  import java.sql.DriverManager
//
//  // Create the JDBC URL without passing in the user and password parameters.
//  val jdbcUrl = s"url/dbname"
//
//  // connect
//  val connection = DriverManager.getConnection(jdbcUrl, "user", "password")
//
//  // execute query
//  val statement = connection.createStatement()
//  val rs = statement.executeQuery("select * from Variant INNER JOIN Variant_has_Consequence ON Variant.id = Variant_has_Consequence.Variant_id INNER JOIN Consequence on Variant_has_Consequence.Consequence_id = Consequence.id;")
//
//  val list = Iterator.from(0).takeWhile(_ => rs.next()).map(_ => rs.getString(1)).toList
//  println(list)
//
//  while(rs.next())
//  {
//    println(rs.getString(1)) //or rs.getString("column name");
//  }
//
//  connection.isClosed()
}


