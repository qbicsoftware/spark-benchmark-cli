Known issues
============

1. Due to a bug in the MariaDB connector and Spark, mariadb in the jdbc URL has to be replaced with mysql.
Please refer to: `#9 <https://github.com/qbicsoftware/spark-benchmark-cli/issues/9>`_.

2. java.io.IOException: No FileSystem for scheme: file #13 when running scark-cli in local mode as a jar
Please refer to: `#13 <https://github.com/qbicsoftware/scark-cli/issues/13>`_.