name := "scala_bootcamp_big_task"

version := "0.1"

scalaVersion := "2.12.14"

val flinkVersion = "1.13.2"
val sparkVersion = "3.1.2"

/**
 * Test dependencies
 */
libraryDependencies ++= Seq (
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "3.0.0" % Test,
)

/**
 * Flink dependencies
 */
libraryDependencies ++= Seq (
  "org.apache.flink" %% "flink-connector-twitter" % flinkVersion,
  "org.apache.flink" %% "flink-clients" % flinkVersion,
  "org.apache.flink" %% "flink-runtime-web" % flinkVersion,
  "commons-logging" % "commons-logging" % "1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
)

/**
 * Spark dependencies
 */
libraryDependencies ++= Seq(
  "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.1",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

/**
 * Other common dependencies (db, json, config)
 */
libraryDependencies ++= Seq (
  "org.reactivemongo" %% "reactivemongo" % "1.0.7",
  //  "org.apache.spark" %% "spark-core" % sparkVersion,
  "com.typesafe.play" %% "play-json" % "2.6.0",
  "com.typesafe" % "config" % "1.4.0"
)