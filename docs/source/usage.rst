Usage
=====

The following sections will guide you through running scark-cli, all possible parameter options and submitting SQL queries to Spark networks.

Running
-------

.. code-block:: bash

    java -jar scark-cli-1.1.0.jar

You should now see the help menu.

Options
-------

.. code-block:: bash

    Usage: Benchmark [-h] -q[=<sqlQuery>] -c=<configFilePath>
    Benchmark Tool for evaluating the performance of a Spark Cluster. Run custom
    SQL Queries inside Spark!
    -s, --spark                run with spark support
    -l, --local                run spark in local mode - requires -s option to be in effect
    -t, --table[=<tables>]     list of tables to execute SQL query in, mandatory if running with spark support
    -d, --driver[=<driver>]    driver to access Database, e.g. org.mariadb.jdbc.Driver, mandatory if running with spark support
    -q, --query[=<sqlQuery>]   SQL query to execute
    - c, --config[=<configFilePath>]
                             database config file path
    -h, --help                 display a help message

Required parameters are:

.. code-block:: bash

    -c, --config=<configFilePath>
    -t  --table[=<table>]
    -q, --query[=<sqlQuery>]

Queries are optionally interactive.
You can either use :code:`-q` to get a prompt for your query or supply a full query when running the tool: :code:`--q[=<sqlQuery>]`.


Spark
-----

*Note* that for scark-cli to be run inside a containerized spark network, such as one set up by `spark-service <https://github.com/qbicsoftware/spark-service>`_,
 it has to be available in the container. Refer to the documentation of your spark container about how to mount volumes and provide access to scark-cli.
A query can be submitted to spark via:

.. code-block:: bash

    /spark/bin/spark-submit --master spark://spark-master:7077 \
    /opt/spark-apps/scark-cli-1.1.0.jar \
    -c /opt/spark-data/database_properties.txt \
    -s \
    -t <table> \
    -q <"query"> \
    -d org.mariadb.jdbc.Driver

Example Query
-------------

.. code-block:: bash

    /spark/bin/spark-submit --master spark://spark-master:7077 \
    /opt/spark-apps/scark-cli-1.1.0.jar \
    -c /opt/spark-data/database_properties.txt \
    -s \
    -t Consequence \
    -q "SELECT id FROM Consequence" \
    -d org.mariadb.jdbc.Driver

Complex Query
-------------

.. code-block:: bash

    /spark/bin/spark-submit --master spark://spark-master:7077 \
    /opt/spark-apps/scark-cli-1.1.0.jar \
    -c /opt/spark-data/database_properties.txt \
    -s \
    -t Consequence Variant Variant_has_Consequence \
    -q "select * from Variant INNER JOIN Variant_has_Consequence ON Variant.id = Variant_has_Consequence.Variant_id INNER JOIN Consequence on Variant_has_Consequence.Consequence_id = Consequence.id" \
    -d org.mariadb.jdbc.Driver
