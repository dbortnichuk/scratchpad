package edu.dbortnichuk.spark

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD

/**
  * Created by dbortnichuk on 21-Oct-16.
  */
object MovieRatings {

  def main(args: Array[String]) {

    val moviesPath: String = edu.dbortnichuk.spark.Main.getPathToRes("edx/data/movies.csv")
    val ratingsPath: String = edu.dbortnichuk.spark.Main.getPathToRes("edx/data/ratings.csv")

    val sparkConf = new SparkConf()
    sparkConf.setAppName("movies-ratings")
    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // val sqlContext = new HiveContext(sc) // for cloud profile only
    val sqlContext = new SQLContext(sc) //

    val moviesSchema = StructType(Array(
      StructField("ID", IntegerType, nullable = true),
      StructField("title", StringType, nullable = true)))
    val ratingsSchema = StructType(Array(
      StructField("userId", IntegerType, nullable = true),
      StructField("movieId", IntegerType, nullable = true),
      StructField("rating", DoubleType, nullable = true)))

    val rawMoviesDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .schema(moviesSchema)
      .option("inferSchema", "false")
      .load(moviesPath)
    val moviesDF = rawMoviesDF.drop("genres").withColumnRenamed("movieId", "ID")

    val rawRatingsDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .schema(ratingsSchema)
      .option("inferSchema", "false")
      .load(ratingsPath)
    var ratingsDF = rawRatingsDF.drop("timestamp")

    val myRawRatingsDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .schema(ratingsSchema)
      .option("inferSchema", "false")
      .load(ratingsPath)
    val myRatingsDF = rawRatingsDF.drop("timestamp")

    ratingsDF = ratingsDF.unionAll(myRatingsDF)

    //moviesDF.cache()
    //ratingsDF.cache()

    //println(s"movies count: ${moviesDF.count()}")
    //println(s"ratings count: ${ratingsDF.count()}")

    val moviesAvgCountDF = ratingsDF.groupBy("movieId")
      .agg(
        count(ratingsDF("rating")).alias("count"),
        avg(ratingsDF("rating")).alias("average"))

    val movieNamesDF = moviesAvgCountDF.join(moviesDF, moviesAvgCountDF("movieId") <=> moviesDF("ID"), "left")
    val movieNamesAvgRatingDF = movieNamesDF.drop("ID")

    //    val moviesWith100RatingOrMore = movieNamesAvgRatingDF.filter(movieNamesAvgRatingDF("count") >= 100)
    //    moviesWith100RatingOrMore.show()

    movieNamesDF.show(500)

    val seed = 1800009193L
    val Array(split80DF, split20DF) = ratingsDF.randomSplit(Array(0.8, 0.2), seed)

    val trainingDF = split80DF.cache()
    //val validationDF = split20aDF.cache()
    val testDF = split20DF.cache()

    val trainingRatingRDD = trainingDF.rdd.map(row => Rating(row.getAs[Int]("userId"), row.getAs[Int]("movieId"), row.getAs[Double]("rating")))
    val testingRatingRDD = testDF.rdd.map(row => Rating(row.getAs[Int]("userId"), row.getAs[Int]("movieId"), row.getAs[Double]("rating")))

    val ranks = Array(2, 8, 12)

    //val model = ALS.train(trainingRatingRDD, 12, 10, 0.01, 4, seed)

    //val userProductsRDD = validationDF.rdd.map(row => (row.getAs[Int]("userId"), row.getAs[Int]("movieId")))
//    val predictRDD = model.predict(userProductsRDD)
//    val predictDF = sqlContext.createDataFrame(predictRDD)
//
//    val regEval = getRegEval
//    val rmse = regEval.evaluate(predictDF)
//
//    println(s"rmse: $rmse")

    val model = new ALS()
      .setRank(12)
      .setIterations(10)
      .setLambda(0.1)
      .setImplicitPrefs(false)
      .setUserBlocks(4)
      .setProductBlocks(4)
      .run(trainingRatingRDD)

     val rmseTraining = computeRmse(model, testingRatingRDD, false)

//    val usersProductsTrainingRDD = trainingRatingRDD.map { case Rating(user, product, rate) =>
//      (user, product)
//    }
//    val predictionsTrainingRDD =
//      model.predict(usersProductsTrainingRDD).map { case Rating(user, product, rate) =>
//        ((user, product), rate)
//      }
//    val ratesAndPreds = trainingRatingRDD.map { case Rating(user, product, rate) =>
//      ((user, product), rate)
//    }.join(predictionsTrainingRDD)
//    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
//      val err = r1 - r2
//      err * err
//    }.mean()
   println("Mean Squared Error = " + rmseTraining)

    val predictRDD = model.predict(testDF.rdd.map(row => (row.getAs[Int]("userId"), row.getAs[Int]("movieId"))))
    val predictDF = sqlContext.createDataFrame(predictRDD)
    //predictDF.show()







    sc.stop()


  }

  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], implicitPrefs: Boolean): Double = {

    def mapPredictedRating(r: Double): Double = {
      if (implicitPrefs) math.max(math.min(r, 1.0), 0.0) else r
    }

    val predictions: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))
    val predictionsAndRatings = predictions.map{ x =>
      ((x.user, x.product), mapPredictedRating(x.rating))
    }.join(data.map(x => ((x.user, x.product), x.rating))).values
    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).mean())
  }


//  def getRegEval = {
//    val regEval = new RegressionEvaluator()
//    regEval.setPredictionCol("prediction")
//    regEval.setLabelCol("rating")
//    regEval.setMetricName("rmse")
//    regEval
//  }

}
