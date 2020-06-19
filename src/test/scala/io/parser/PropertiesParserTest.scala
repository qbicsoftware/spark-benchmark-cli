package io.parser

import org.scalatest.funsuite.AnyFunSuite

class PropertiesParserTest extends AnyFunSuite{
  val DATABASE_PROPERTIES_TEST_GOOD: String = getClass.getResource("/database_properties_test_good.txt").getPath
  val DATABASE_PROPERTIES_TEST_BAD: String = getClass.getResource("/database_properties_test_bad.txt").getPath

  test ("PropertiesParser should return the corresponding DatabaseProperties") {
      val databaseProperties = PropertiesParser.readPropertiesFile(DATABASE_PROPERTIES_TEST_GOOD)

      assert(databaseProperties.jdbcURL === "jdbc:mysql://fancyurl/oncostore")
      assert(databaseProperties.port === "8080")
      assert(databaseProperties.databaseName === "oncostore")
      assert(databaseProperties.password === "supersecurepassword")
      assert(databaseProperties.user === "oncoloader")
    }

    test ("PropertiesParser should throw an IllegalArugment Exception due to malformed URL") {
      assertThrows[IllegalArgumentException] {
        val databaseProperties = PropertiesParser.readPropertiesFile(DATABASE_PROPERTIES_TEST_BAD)
      }
    }

}
