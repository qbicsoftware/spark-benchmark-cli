[![Build Status](https://travis-ci.com/qbicsoftware/spark-benchmark-cli.svg?branch=development)](https://travis-ci.com/qbicsoftware/spark-benchmark-cli)

# spark-benchmark-cli
Here, various expensive queries and operations for spark benchmarking are defined.

## Building
SBT assembly plugin is configured. Hence, in the project root:
```bash
sbt assembly
```
will build the fat jar. The result will be written to /target/$scala-version/$name-assembly-$version.jar

## Running
```bash
java -jar spark-benchmark-cli-assembly-$version.jar
```

## Usage
### Options
```bash
Usage: Benchmark [-h] -q[=<sqlQuery>] -c=<configFilePath>
Benchmark Tool for evaluating the performance of a Spark Cluster. Run custom
SQL Queries inside Spark!
  -s, --spark                run with spark support 
  -l, --local                run spark in local mode - requires -s option to be in effect
  -q, --query[=<sqlQuery>]   SQL query to execute
  -c, --config=<configFilePath>
                             database config file path
  -h, --help                 display a help message
```
Required parameters are:
```
-c, --config=<configFilePath>
-q, --query
```
Queries are optionally interactive.
You can either use ```-q``` to get a prompt for your query or supply a full query when running the tool: ```--q[=<sqlQuery>]```.

## Spark
A query can be submitted to spark via:
```bash
/spark/bin/spark-submit --master spark://spark-master:7077 \
/opt/spark-apps/spark-benchmark-cli-assembly-0.1-2.12.jar -c /opt/spark-data/database_properties.txt -s -t <table> -q <"query"> 
```

## Example Query
```bash
/spark/bin/spark-submit --master spark://spark-master:7077 \
/opt/spark-apps/spark-benchmark-cli-assembly-0.1-2.12.jar -s -l -c /opt/spark-data/database_properties.txt -t Consequence -q "SELECT id FROM Consequence"
```

## Tests
Run tests <b>inside the sbt console</b> from the root project directory using:
```bash
test
```
