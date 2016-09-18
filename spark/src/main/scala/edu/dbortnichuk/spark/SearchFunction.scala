package edu.dbortnichuk.spark

import org.apache.spark.rdd.RDD

/**
  * Created by dbortnichuk on 18-Sep-16.
  */
class SearchFunctions(val query: String) {
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }
  def getMatchesFunctionReference(rdd: RDD[String]): RDD[String] = {
    // Problem: "isMatch" means "this.isMatch", so we pass all of "this"
    rdd.filter(isMatch)
  }
  def getMatchesFieldReference(rdd: RDD[String]): RDD[String] = {
    // Problem: "query" means "this.query", so we pass all of "this"
    rdd.flatMap(x => x.split(query))
  }
  def getMatchesNoReference(rdd: RDD[String]): RDD[String] = {
    // Safe: extract just the field we need into a local variable
    val query_ = this.query
    rdd.flatMap(x => x.split(query_))
  }
}

object SearchFunctions{
  def apply(query: String): SearchFunctions = new SearchFunctions(query)
}
