package utils

import java.io.{FileWriter, PrintWriter}
import scala.collection.mutable
import scala.io.Source

class CoPurchaseAnalysisPlainScala {

  private def getAllPairs(products: List[Int]): List[(Int, Int)] = {
    for {
      (p1, idx1) <- products.zipWithIndex
      p2 <- products.drop(idx1 + 1)
    } yield if (p1 < p2) (p1, p2) else (p2, p1)
  }

  private def writeCoPurchaseResultsToCsv(results: Map[(Int, Int), Int], outputFileName: String): Unit = {
    val writer = new PrintWriter(new FileWriter(outputFileName))
    try {
      results.foreach { case ((product1, product2), count) =>
        writer.println(s"$product1,$product2,$count")
      }
    } finally {
      writer.close()
    }
    println(s"Co-purchase analysis results saved to $outputFileName")
  }

  def countCoPurhcases(in: String, out: String): Unit = {
    val source = Source.fromFile(in)
    val coPurchaseCounts = mutable.Map[(Int, Int), Int]().withDefaultValue(0)

    val groupedLines = source
      .getLines()
      .map(_.split(",").map(_.trim.toInt))
      .toList
      .groupBy(_(0))

    groupedLines.foreach { case (_, productRows) =>
      val products = productRows.map(_(1)).distinct
      val pairs = getAllPairs(products)
      pairs.foreach { pair =>
        coPurchaseCounts(pair) += 1
      }
    }

    source.close()

    writeCoPurchaseResultsToCsv(coPurchaseCounts.toMap, out)
  }
}
