import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Main {

  private def coPurchaseAnalysisSpark(in: String, out: String): Unit = {
    val spark = SparkSession.builder()
      .appName("cpa")
      .master("local[*]")
      .config("spark.eventLog.enabled", "true")
      .config("spark.eventLog.dir", "/tmp/spark-events")
      .getOrCreate()

    val partitions = spark.sparkContext.defaultParallelism * 2

    val rawData: RDD[(Int, Int)] = spark.sparkContext
      .textFile(in)
      .map(line => {
        val parts = line.split(",")
        (parts(0).toInt, parts(1).toInt)
      })

    val hashPartitionedData: RDD[(Int, Int)] = rawData.partitionBy(new HashPartitioner(partitions))
    val groupedByOrder: RDD[(Int, Iterable[Int])] = hashPartitionedData.groupByKey()

    val productPairs: RDD[((Int, Int), Int)] = groupedByOrder.flatMap {
      case (_, products) =>
        val productList = products.toSet.toIndexedSeq
        for {
          i <- productList
          j <- productList if i < j
        } yield ((i, j), 1)
    }

    val coPurchaseCounts: RDD[((Int, Int), Int)] = productPairs.reduceByKey(_ + _)

    coPurchaseCounts
      .map { case ((p1, p2), c) => s"$p1,$p2,$c" }
      .coalesce(1)
      .saveAsTextFile(out)

    spark.stop()
  }

  def main(args: Array[String]): Unit = {
    val bucketName = "cpa-scp-bucket"
    val in = if (args.nonEmpty) args(0) else "gs://" + bucketName + "/order_products.csv"
    val out = if (args.length == 2) args(1) else "gs://" + bucketName + "/output"

    if (args.length != 2) {
      println(f"Invalid number of arguments: ${args.length}%d\nTwo required: 1: <input file> 2: <output file>")
      println(f"Using defaults <${in}%s> <${out}%s>")
    }

    coPurchaseAnalysisSpark(in, out)
  }
}
