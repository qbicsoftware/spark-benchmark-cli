# spark-benchmark-cli
Here, various expensive queries and operations for spark benchmarking are defined.

# Building
SBT assembly plugin is configured. Hence, in the project root:
```bash
sbt assembly
```
will build the fat jar. The result will be written to /target/$scala-version/$name-assembly-$version.jar

# Running
Run it as usual:
```bash
java -jar $name-assembly-$version.jar
```
