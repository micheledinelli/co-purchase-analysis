scalaVersion := "2.13.15"

name := "co-purchase-analysis"
organization := "edu.unibo.cs.scp"
version := "0.0.1"

val sparkVersion = "3.5.5"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.4.0",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
)
