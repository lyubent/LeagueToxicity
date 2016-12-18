name := "LeagueToxicity"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.2"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.2"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

