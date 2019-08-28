Installation
============

The following sections will guide you through building, testing and obtaining the latest release of scark-cli.

Obtaining the latest release
----------------------------

If you're not a developer and not interested in building the code itself, simply download the `latest release <https://github.com/qbicsoftware/scark-cli/releases>`_.
Next, refer to `usage <usage.html>`_ for detailed instructions about how to submit SQL queries locally and to the spark network.

Getting the code
----------------

The code is hosted on github. Clone the repository via:

.. code-block:: bash

    git clone https://github.com/qbicsoftware/scark-cli

Building
--------

scark-cli is build with `sbt <https://www.scala-sbt.org/>`_. Furthermore, the sbt assembly plugin is configured allowing for fat jars to be build:

.. code-block:: bash

    sbt assembly

will build the fat jar. The result will be written to 

.. code-block:: bash

    /target/$scala-version/$name-assembly-$version.jar

Testing
-------

Run tests *inside the sbt console* from the root project directory using:

.. code-block:: bash

    test

Next, refer to `usage <usage.html>`_ for detailed instructions about how to submit SQL queries locally and to the spark network.
