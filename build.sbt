import sbtrelease._
import ReleasePlugin._
import ReleaseStateTransformations._

sbtPlugin := true

name := "sbt-docker-compose"

organization := "com.tapad"

scalaVersion := "2.12.16"

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "3.5.0",
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0" % Test,
  "org.yaml" % "snakeyaml" % "1.30",
  "org.mockito" % "mockito-all" % "1.10.19" % Test
)

publishTo := {
  val nexus = "https://oss.sonatype.org"
  if (isSnapshot.value)
    Some("snapshots" at s"$nexus/content/repositories/snapshots")
  else
    Some("releases" at s"$nexus/service/local/staging/deploy/maven2")
}

publishMavenStyle := true

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

pomExtra := {
  <url>http://github.com/Tapad/sbt-docker-compose</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://opensource.org/licenses/BSD-3-Clause</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:Tapad/sbt-docker-compose.git</url>
      <connection>scm:git:git@github.com:Tapad/sbt-docker-compose.git</connection>
    </scm>
    <developers>
      <developer>
        <id>kurt.kopchik@tapad.com</id>
        <name>Kurt Kopchik</name>
        <url>http://github.com/kurtkopchik</url>
      </developer>
    </developers>
  }

import scalariform.formatter.preferences._

releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  releaseStepCommandAndRemaining("^test"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("^publishSigned"),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
