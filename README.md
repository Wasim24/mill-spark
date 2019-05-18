# mill-spark

Build projects against multiple versions of Spark and Scala!

## Quickstart
Here is an example building a project `foo` against the following combinations
of Scala and Spark:

- Scala 2.11.8 with Spark 2.3.0
- Scala 2.11.8 with Spark 2.4.0
- Scala 2.12.4 with Spark 2.4.0

```scala
import mill._, scalalib._
import $ivy.`com.nikvanderhoof::mill-spark:0.1.0`
import com.nikvanderhoof.mill._

val buildMatrix = for {
    scala <- Seq("2.11.8", "2.12.4")
    spark <- Seq("2.3.0", "2.4.0")
    if !(scala >= "2.12.0" && spark < "2.4.0"
} yield (scala, spark)

object foo extends Cross[FooModule](buildMatrix: _*)
class FooModule(val crossScalaVersion: String, val crossSparkVersion: String)
extends CrossScalaSparkModule {
  def ivyDeps = Agg(spark"sql")  // this is shorthand for ivy"org.apache.spark::spark-sql:${crossSparkVersion}"
}
```

Your build should be structured like so:

```
.
├── build.sc
└── foo
    ├── src                # Source common to all version
    ├── src-2.11__2.4      # Sources for Scala 2.11.x and Spark 2.4.x
    ├── src-2.11.8__2.3.0  # Sources for Scala 2.11.8 and Spark 2.3.0
    ├── src-2.12__2.4      # Sources for Scala 2.12.x and Spark 2.4.x
    └── src-2__2.4         # Sources for Scala 2.x.y  and Spark 2.4.x
```

To build a specific version:

```
mill "foo[2.11.8,2.3.0].compile"   # Scala 2.11.8 and Spark 2.3.0
mill "foo[2.12.4,2.4.0].compile"   # Scala 2.12.4 and Spark 2.4.0
```

To build all versions:

```
mill __.compile
```
