[![Build Status](https://travis-ci.com/qbicsoftware/spark-benchmark-cli.svg?branch=development)](https://travis-ci.com/qbicsoftware/spark-benchmark-cli)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

# spark-benchmark-cli
A tool for submitting SQL queries to a Spark Cluster. Various benchmarking statistics will be calculated.    
Currently MariaDB is supported out of the box.

## Building
SBT assembly plugin is configured. Hence, in the project root:
```bash
sbt assembly
```
will build the fat jar. The result will be written to ```/target/$scala-version/$name-assembly-$version.jar```

## Running
```bash
java -jar scark-cli-1.0.0.jar
```

## Usage
### Options
```bash
Usage: Benchmark [-h] -q[=<sqlQuery>] -c=<configFilePath>
Benchmark Tool for evaluating the performance of a Spark Cluster. Run custom
SQL Queries inside Spark!
  -s, --spark                run with spark support 
  -l, --local                run spark in local mode - requires -s option to be in effect
  -t, --table[=<table>]      table to execute SQL query in, mandatory if running with spark support
  -d, --driver[=<driver>]    driver to access Database, e.g. org.mariadb.jdbc.Driver, mandatory if running with spark support 
  -q, --query[=<sqlQuery>]   SQL query to execute
  -c, --config[=<configFilePath>]
                             database config file path
  -h, --help                 display a help message
```
Required parameters are:
```
-c, --config=<configFilePath>
-t  --table[=<table>]
-q, --query[=<sqlQuery>]
```
Queries are optionally interactive.
You can either use ```-q``` to get a prompt for your query or supply a full query when running the tool: ```--q[=<sqlQuery>]```.

## Spark
A query can be submitted to spark via:
```bash
/spark/bin/spark-submit --master spark://spark-master:7077 \
/opt/spark-apps/scark-cli-1.0.0.jar -s -d org.mariadb.jdbc.Driver -c /opt/spark-data/database_properties.txt -t <table> -q <"query"> 
```

## Example Query
```bash
/spark/bin/spark-submit --master spark://spark-master:7077 \
/opt/spark-apps/scark-cli-1.0.0.jar -s -d org.mariadb.jdbc.Driver -c /opt/spark-data/database_properties.txt -t Consequence -q "SELECT id FROM Consequence"
```

## Loading multiple tables for JOIN operations
To load multiple use the ```-t``` option with an alias:    
```
-t "(select * from Variant INNER JOIN Variant_has_Consequence ON Variant.id = Variant_has_Consequence.Variant_id) as t"
```

This will load all required tables and allow for queries.

## Tests
Run tests <b>inside the sbt console</b> from the root project directory using:
```bash
test
```

## Known issues
Due to a bug in the MariaDB connector and Spark, mariadb in the jdbc URL has to be replaced with mysql.    
Please refer to: https://github.com/qbicsoftware/spark-benchmark-cli/issues/9 .
