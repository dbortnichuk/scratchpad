package edu.dbortnichuk.spark

import java.io.{FileReader, FileWriter}

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{DecisionTreeRegressor, LinearRegression, RandomForestRegressor}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source
import scala.util.Try

/**
  * Created by dbort on 19.10.2016.
  */
object PowerPlant {

  def main(args: Array[String]): Unit = {

    val filePowerPlantRaw: String = edu.dbortnichuk.spark.Main.getPathToRes("edx/data/power-plant-raw.csv")
    val filePowerPlant: String = edu.dbortnichuk.spark.Main.getPathToRes("edx/data/") + "power-plant.csv"

    //    val fw = new FileWriter(filePowerPlant, true)
    //    try {
    //      Source.fromFile(filePowerPlantRaw).getLines().foreach(s => fw.write(s.replaceAll(",", ".").replaceAll(";", ",") + "\n"))
    //    } finally {
    //      fw.close()
    //    }

    val sparkConf = new SparkConf()
    sparkConf.setAppName("power-plant")
    sparkConf.setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // val sqlContext = new HiveContext(sc) // for cloud profile only
    val sqlContext = new SQLContext(sc) //

    val customSchema = StructType(Array(
      StructField("AT", DoubleType, nullable = true),
      StructField("V", DoubleType, nullable = true),
      StructField("AP", DoubleType, nullable = true),
      StructField("RH", DoubleType, nullable = true),
      StructField("PE", DoubleType, nullable = true)))

    //extract data
    val ppDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .schema(customSchema)
      // .option("inferSchema", "true")
      .load(filePowerPlant)
    //    df.printSchema()
    //    df.show()
    //    df.dtypes.foreach(println)

    //set mechanics for building feature vector
    val vecrorizer = new VectorAssembler()
    vecrorizer.setInputCols(Array("AT", "V", "AP", "RH"))
    vecrorizer.setOutputCol("features")

    //divide data into training and testing sets
    val splitDF = ppDF.randomSplit(Array(0.2, 0.8), 1800009193L)
    val testSetDF = splitDF(0).cache()
    val trainingSetDF = splitDF(1).cache()

    //setup training algorithm
    val lr = new LinearRegression()
    lr
      .setPredictionCol("Predicted_PE")
      .setLabelCol("PE")
      .setMaxIter(100)
      .setRegParam(0.1)

    //    val lrMod = lr.fit(trainingSetDF)
    //    val intercept = lrMod.intercept
    //    val weights = lrMod.coefficients
    //    val featuresNoLabel = df.columns.filter(s => !s.equals("PE"))
    //    val coefficients = weights.toArray.zip(featuresNoLabel)
    //    coefficients.foreach(println)

    //setup ml pipeline
    val lrPipeline = new Pipeline()
    lrPipeline.setStages(Array(vecrorizer, lr))

    //build model
    val lrModel = lrPipeline.fit(trainingSetDF)
    var predictionsAndLabelsDF = lrModel.transform(testSetDF).select("AT", "V", "AP", "RH", "PE", "Predicted_PE")

    val regEval = new RegressionEvaluator()
    regEval.setPredictionCol("Predicted_PE")
    regEval.setLabelCol("PE")
    regEval.setMetricName("rmse")
    val rmse = regEval.evaluate(predictionsAndLabelsDF)

    val regEval2 = new RegressionEvaluator()
    regEval2.setPredictionCol("Predicted_PE")
    regEval2.setLabelCol("PE")
    regEval2.setMetricName("r2")
    val r2 = regEval2.evaluate(predictionsAndLabelsDF)

    val ppDFRMSE = predictionsAndLabelsDF.selectExpr("PE", "Predicted_PE", "PE - Predicted_PE Residual_Error", s"(PE - Predicted_PE) / $rmse Within_RSME")
    ppDFRMSE.show()

    val crossval = new CrossValidator()
    crossval.setEstimator(lrPipeline)
    crossval.setEvaluator(regEval)
    crossval.setNumFolds(3)

    val regParam: Array[Double] = (1 to 10).map(x => x.toDouble / 100).toArray
    var paramGrid = new ParamGridBuilder().addGrid(lr.regParam, regParam).build()

    crossval.setEstimatorParamMaps(paramGrid)
    val cvModel = crossval.fit(trainingSetDF).bestModel

    //Now let's use cvModel to compute an evaluation metric for our test dataset: testSetDF
    predictionsAndLabelsDF = cvModel.transform(testSetDF).select("AT", "V", "AP", "RH", "PE", "Predicted_PE")

    //Run the previously created RMSE evaluator, regEval, on the predictionsAndLabelsDF DataFrame
    val rmseNew = regEval.evaluate(predictionsAndLabelsDF)
    val r2New = regEval2.evaluate(predictionsAndLabelsDF)

    println("Original Root Mean Squared Error: %.2f".format(rmse))
    println("New Root Mean Squared Error: %.2f".format(rmseNew))
    println("Old r2: %.2f".format(r2))
    println("New r2: %.2f".format(r2New))
    //print("Regularization parameter of the best model: %.2f".format())
    //cvModel.parent.extractParamMap().toSeq.foreach(println)

    //Create a DecisionTreeRegressor
    val dt = new DecisionTreeRegressor()
    dt.setLabelCol("PE")
    dt.setPredictionCol("Predicted_PE")
    dt.setFeaturesCol("features")
    dt.setMaxBins(100)

    //Create a Pipeline
    val dtPipeline = new Pipeline()
    dtPipeline.setStages(Array(vecrorizer, dt))

    crossval.setEstimator(dtPipeline)

    //Let's tune over our dt.maxDepth parameter on the values 2 and 3, create a paramter grid using the ParamGridBuilder
    paramGrid = new ParamGridBuilder().addGrid(dt.maxDepth, Array(2, 3)).build()

    //Add the grid to the CrossValidator
    crossval.setEstimatorParamMaps(paramGrid)

    //Now let's find and return the best model
    val dtModel = crossval.fit(trainingSetDF).bestModel

    //Now let's use dtModel to compute an evaluation metric for our test dataset: testSetDF
    predictionsAndLabelsDF = dtModel.transform(testSetDF).select("AT", "V", "AP", "RH", "PE", "Predicted_PE")

    //Run the previously created RMSE evaluator, regEval, on the predictionsAndLabelsDF DataFrame
    val rmseDT = regEval.evaluate(predictionsAndLabelsDF)
    //Now let's compute the r2 evaluation metric for our test dataset
    val r2DT = regEval2.evaluate(predictionsAndLabelsDF)

    println("LR Root Mean Squared Error: %.2f".format(rmseNew))
    println("DT Root Mean Squared Error: %.2f".format(rmseDT))
    println("LR r2: %.2f".format(r2New))
    println("DT r2: %.2f".format(r2DT))

    //Create a RandomForestRegressor
    val rf = new RandomForestRegressor()
    rf.setLabelCol("PE")
    rf.setFeaturesCol("Predicted_PE")
    rf.setFeaturesCol("features")
    rf.setSeed(100088121L)
    rf.setMaxDepth(8)
    rf.setNumTrees(30)

    //Create a Pipeline
    val rfPipeline = new Pipeline()

    //Set the stages of the Pipeline
    rfPipeline.setStages(Array(vecrorizer, rf))

    //val crossval1 = new CrossValidator()

    //Let's just reuse our CrossValidator with the new rfPipeline,  RegressionEvaluator regEval, and 3 fold cross validation
    crossval.setEstimator(rfPipeline)

    //Let's tune over our rf.maxBins parameter on the values 50 and 100, create a parameter grid using the ParamGridBuilder
    paramGrid = new ParamGridBuilder().addGrid(rf.maxBins, Array(50, 100)).build()
    //Add the grid to the CrossValidator
    //crossval1.setEvaluator(regEval)
    crossval.setEstimatorParamMaps(paramGrid)
    //crossval1.setNumFolds(3)

    //Now let's find and return the best model
    val rfModel = crossval.fit(trainingSetDF).bestModel

    //Now let's use rfModel to compute an evaluation metric for our test dataset: testSetDF
    predictionsAndLabelsDF = rfModel.transform(testSetDF).select("AT", "V", "AP", "RH", "PE", "Predicted_PE")

    //Run the previously created RMSE evaluator, regEval, on the predictionsAndLabelsDF DataFrame
    val rmseRF = regEval.evaluate(predictionsAndLabelsDF)
    val r2RF = regEval2.evaluate(predictionsAndLabelsDF)

    println("LR Root Mean Squared Error: %.2f".format(rmseNew))
    println("DT Root Mean Squared Error: %.2f".format(rmseDT))
    println("RF Root Mean Squared Error: %.2f".format(rmseRF))
    println("LR r2: %.2f".format(r2New))
    println("DT r2: %.2f".format(r2DT))
    println("RF r2: %.2f".format(r2RF))

    sc.stop()


  }

}
