import mill._, scalalib._, publish._

object `mill-spark` extends ScalaModule with PublishModule {
  def scalaVersion = "2.12.8"

  def millVersion = "0.3.6"

  def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-main:${millVersion}",
    ivy"com.lihaoyi::mill-scalalib:${millVersion}"
  )

  def publishVersion = "0.1.0"

  def artifactName = "mill-spark"

  override def pomSettings = PomSettings(
    description = "Build projects against multiple Spark and Scala versions.",
    organization = "com.nikvanderhoof",
    url = "https://www.github.com/nvander1/mill-spark",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("nvander1", "mill-spark"),
    developers = Seq(
      Developer("nvander1", "Nikolas Vanderhoof", "https://www.github.com/nvander1")
    )
  )
}
