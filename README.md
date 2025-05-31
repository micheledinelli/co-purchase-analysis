# co-purchase-analysis

[![scala](https://img.shields.io/badge/Scala-2.12.18-DC322F?logo=Scala)](https://www.scala-lang.org/download/2.12.18.html)
[![spark](https://img.shields.io/badge/Apache_Spark-3.5.5-E25A1C.svg?style=plain&logo=Apache-Spark&logoColor=white)](https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.13/3.5.5)
[![java](https://img.shields.io/badge/JDK-17.0.12-E25A1C.svg?style=plain&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![sbt](https://img.shields.io/badge/sbt-1.10.11-default.svg?style=plain)]()
![gc](https://img.shields.io/badge/Google%20Cloud-4285F4.svg?style=plain&logo=Google-Cloud&logoColor=white)

[//]: # (&#40;![databricks]&#40;https://img.shields.io/badge/Databricks-FF3621.svg?style=plain&logo=Databricks&logoColor=white&#41;&#41;)

Project realized for Scalable and Cloud Programming (81942) university course @unibo.

## How to Run Locally

Ensure to have `Java`, `Scala` and `Sbt` installed, the versions used for this project are listed in the header badges.  

Export the following Java options

```sh
export JAVA_OPTS="--add-opens=java.base/java.lang.invoke=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
```

### Option 1

Start sbt server

```sh
sbt
```

Then run the project from within the sbt shell

```sh
run <opt_input_file> <opt_output_file>
```

[test.csv](./test.csv) is a way smaller version of the original dataset and it's used only for testing purposes.

### Option 2

Submit a Spark job by compiling the project using the command

```sh
sbt clean compile package
```

This produces a Jar under `/target`. Submit it to Spark with

```sh
spark-submit target/<JAR_NAME> <opt_input_file> <opt_output_file>
```

---

Both options will start Spark UI on [localhost:4040](http://localhost:4040/) if available.