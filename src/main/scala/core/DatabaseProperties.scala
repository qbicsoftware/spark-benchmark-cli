package core

case class DatabaseProperties(jdbcURL: String,
                              user: String,
                              password: String,
                              port: String = "3306",
                              databaseName: String)


