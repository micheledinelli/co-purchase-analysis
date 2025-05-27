import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import utils.Timer.time

object Main {
  private def coPurchaseAnalysisSpark(in: String, out: String): Unit = {
    val spark = SparkSession.builder()
      .appName("cpa")
      .master("local[*]")
//      .config("spark.driver.memory", "8g")
//      .config("spark.executor.memory", "8g")
//      .config("spark.sql.shuffle.partitions", "200")
//      .config("spark.memory.fraction", "0.8")
      .getOrCreate()

    val cores = spark.conf.get("spark.executor.cores", "4").toInt
    val nodes = spark.conf.get("spark.executor.instances", "4").toInt
    val numPartitions = math.max(cores * nodes * 2, spark.sparkContext.defaultParallelism * 2)

    val rawData: RDD[(Int, Int)] = spark.sparkContext
      .textFile(in)
      .map(line => {
        val parts = line.split(",")
        (parts(0).toInt, parts(1).toInt)
      })

    val partitionedData = rawData.partitionBy(new HashPartitioner(numPartitions))
    val groupedData = partitionedData.groupByKey()

    val productPairs: RDD[((Int, Int), Int)] = groupedData.
      flatMap { case
        (_, products) =>
        val productList = products.toList
        for {
          i <- productList
          j <- productList if i < j
        } yield ((i, j), 1)
      }

    val repartitionedPairs = productPairs.partitionBy(new HashPartitioner(numPartitions))

    val coPurchaseCounts = repartitionedPairs.reduceByKey(_ + _)
    coPurchaseCounts
      .map { case ((p1, p2), count) => s"$p1,$p2,$count" }
      .repartition(1)
      .saveAsTextFile(out)

    spark.stop()
  }

  def main(args: Array[String]): Unit = {
    val in = if (args.nonEmpty) args(0) else "./test.csv"
    val out: String = if (args.length == 2) args(1) else "./output"

    if (args.length != 2) {
      System.out.printf("Invalid number of arguments: %d\nTwo required: 1: <input file> 2: <output file>", args.length)
      System.out.printf("Using defaults <%s> <%s>", in, out)
    }

    time(coPurchaseAnalysisSpark(in, out))
  }
}

//object Main extends App {
//
//  val inputFile = "./sherlock.txt"
//
//  private def wordCountScala(fileName: String) = {
//    val source = Source.fromFile(fileName)
//    val allWords = source.getLines.toList.
//      flatMap(line => line.split(" ")).
//      map(w => (w.filter(_.isLetter).toUpperCase))
//
//    val emptyMap = Map[String, Int]() withDefaultValue 0
//    allWords.foldLeft(emptyMap)((a, w) => (a) + (w -> (a(w) + 1)))
//  }
//
//  private def wordCountSpark(fileName: String) = {
//    val conf = new SparkConf().setAppName("wordCount").
//      setMaster("local[6]")
//    val allWordsWithOne = new SparkContext(conf).textFile(fileName).
//      flatMap(line => line.split(" ")).
//      map(w => (w.filter(_.isLetter).toUpperCase, 1))
//    allWordsWithOne.reduceByKey((x, y) => x + y).collectAsMap()
//  }
//  time(wordCountScala(inputFile))
//  time(wordCountSpark(inputFile))
//}