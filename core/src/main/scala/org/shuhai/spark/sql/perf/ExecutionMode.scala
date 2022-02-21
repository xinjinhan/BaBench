package org.shuhai.spark.sql.perf

/**
 * Describes how a given Spark benchmark should be run (i.e. should the results be collected to
 * the driver or just computed on the executors.
 */
trait ExecutionMode extends Serializable
case object ExecutionMode {
  /** Benchmark run by collecting queries results  (e.g. rdd.collect()) */
  case object CollectResults extends ExecutionMode {
    override def toString: String = "collect"
  }

  /** Benchmark run by iterating through the queries results rows (e.g. rdd.foreach(row => Unit)) */
  case object ForeachResults extends ExecutionMode {
    override def toString: String = "foreach"
  }

  /** Benchmark run by saving the output of each query as a parquet file. */
  case class WriteParquet(location: String) extends ExecutionMode {
    override def toString: String = "saveToParquet"
  }

  /**
   * Benchmark run by calculating the sum of the hash value of all rows. This is used to check
   * query results do not change.
   */
  case object HashResults extends ExecutionMode {
    override def toString: String = "hash"
  }

  /** Results from Spark perf */
  case object SparkPerfResults extends ExecutionMode {
    override def toString: String = "sparkPerf"
  }
}