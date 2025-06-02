# co-purchase-analysis

[![scala](https://img.shields.io/badge/Scala-2.12.18-DC322F?logo=Scala)](https://www.scala-lang.org/download/2.12.18.html)
[![spark](https://img.shields.io/badge/Apache_Spark-3.5.5-E25A1C.svg?style=plain&logo=Apache-Spark&logoColor=white)](https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.13/3.5.5)
[![java](https://img.shields.io/badge/JDK-17.0.12-E25A1C.svg?style=plain&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![sbt](https://img.shields.io/badge/sbt-1.10.11-default.svg?style=plain)]()
![gc](https://img.shields.io/badge/Google%20Cloud-4285F4.svg?style=plain&logo=Google-Cloud&logoColor=white)

Project realized for Scalable and Cloud Programming (81942) university course @unibo.

Documentation is available [here](https://micheledinelli.github.io/co-purchase-analysis/report.pdf).

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
run <input_file> <output_directory>
```

[test.csv](order_products_example.csv) is a way smaller version of the original dataset and it's used only for testing purposes.

### Option 2

Submit a Spark job by compiling the project using the command

```sh
sbt clean compile package
```

This produces a Jar under `/target/scala-2.12`. Submit it to Spark with

```sh
spark-submit <JAR_PATH> <input_file> <output_dir>
```

---

Both options will start Spark UI on [localhost:4040](http://localhost:4040/).

### How to Check Results

```shell
awk -F, 'NR == 1 || $3 > max { max = $3; line = $0 } END { print line }' <output_dir>/part-00000

# this will output something like
# 13176,47209,62341

# using the sample dataset the command should yield
# 12,14,3
```

## Dataproc Testing

Set-up an `.env` file with these 3 variables (refer to [env.example](.env.example)):

- `PROJECT`
- `REGION`
- `BUCKET`

These can be exported using the command `. ./export-env.sh`, or manually.

Create a bucket on Google Cloud and provide a .csv file following the format of [order_products_example.csv](./order_products_example.csv). 
Ensure that your service account has the roles `Dataproc Worker` and `Storage Admin`.

Then run 

1. `./update-jar.sh` this will compile, package and upload a jar file to Google Cloud buckets.
2. `./init-cluster.sh <N>` this will initialize a cluster with `<N>` workers.
3. `./submit.job.sh <CLUSTER>` this will submit a job to a `<CLUSTER>` using the exported `BUCKET` variable as data-source for the jar and the dataset 
4. `./clean.sh <CLUSTER> [--buckets]` this will delete the cluster and optionally ALL the buckets.


