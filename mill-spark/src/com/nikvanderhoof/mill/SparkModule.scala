package com.nikvanderhoof.mill

import mill._, scalalib._
import mill.moduledefs.Cacher
import mill.eval.PathRef


object CrossBase {
  def cartesianProduct[T](seqs: Seq[Seq[T]]): Seq[Seq[T]] = {
    seqs.foldLeft(Seq(Seq.empty[T]))((b, a) => b.flatMap(i => a.map(j => i ++ Seq(j))))
  }

  def parts(cs: Seq[String]) = cs.map(c => c.split('.').inits.filter(_.nonEmpty).toSeq)

  def crossVersionPaths(versions: Seq[String], f: String => ammonite.ops.Path) =
    cartesianProduct(parts(versions)).map { segmentGroups =>
      segmentGroups.map(_.mkString("."))
    }.map(xs => PathRef(f(xs.mkString("__"))))
}

trait SparkModule { self: ScalaModule =>
  implicit class SparkDepSyntax(ctx: StringContext) {
    def spark(args: Any*) = Dep.parse(
      s"org.apache.spark::spark-${ctx.parts.mkString}:${sparkVersion}"
    )
  }
  def sparkVersion: String
}

trait CrossScalaSparkModule extends ScalaModule with SparkModule {
  def crossScalaVersion: String
  def crossSparkVersion: String
  def scalaVersion = crossScalaVersion
  def sparkVersion = crossSparkVersion
  override def millSourcePath = super.millSourcePath / ammonite.ops.up / ammonite.ops.up
  override def sources = T.sources {
    super.sources() ++
    CrossBase.crossVersionPaths(
      Seq(crossScalaVersion, crossSparkVersion), s => millSourcePath / s"src-$s")
  }
  trait Tests extends super.Tests {
    override def sources = T.sources {
      super.sources() ++
      CrossBase.crossVersionPaths(
        Seq(crossScalaVersion, crossSparkVersion), s => millSourcePath / s"src-$s")
    }
  }
}
