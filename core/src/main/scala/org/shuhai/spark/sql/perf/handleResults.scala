package org.shuhai.spark.sql.perf

import org.apache.spark.sql.SQLContext

case class Results(resultsLocation: String, @transient sqlContext: SQLContext) {
  def allResults =
    sqlContext.read.json(
      sqlContext.sparkContext.textFile(s"$resultsLocation/*/"))
}
