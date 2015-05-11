// ### BASIC SETTINGS ### //

organization          := "io.jvm"

name                  := "jvm-privatization"

version               := "0.0.1"

crossScalaVersions    := Seq("2.11.4")

scalaVersion          := crossScalaVersions.value.head

organization          := "io.jvm"

publishTo             := Some(if (version.value endsWith "-SNAPSHOT") Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging)

licenses              += ("BSD-style", url("http://opensource.org/licenses/BSD-3-Clause"))

startYear             := Some(2014)

scmInfo               := Some(ScmInfo(url("https://github.com/melezov/jvm-privatization"), "scm:git:https://github.com/melezov/jvm-privatization.git"))

pomExtra              ~= (_ ++ {Developers.toXml})

publishMavenStyle     := true

pomIncludeRepository  := { _ => false }

homepage              := Some(url("http://jvm.io"))

// ### DEPENDENCIES ### //

libraryDependencies   ++= Seq(
  "org.specs2" %% "specs2-core" % "2.4.11" % "test"
, "commons-io" % "commons-io" % "2.4" % "test"
)

// ### COMPILE SETTINGS ### //

scalacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:existentials"
, "-language:implicitConversions"
, "-language:postfixOps"
, "-language:reflectiveCalls"
, "-optimise"
, "-unchecked"
, "-Xcheckinit"
, "-Xlint"
, "-Xmax-classfile-name", "72"
, "-Xno-forwarders"
, "-Xverify"
, "-Yclosure-elim"
, "-Yconst-opt"
, "-Ydead-code"
, "-Yinline-warnings"
, "-Yinline"
, "-Yrepl-sync"
, "-Ywarn-adapted-args"
, "-Ywarn-dead-code"
, "-Ywarn-inaccessible"
, "-Ywarn-infer-any"
, "-Ywarn-nullary-override"
, "-Ywarn-nullary-unit"
, "-Ywarn-numeric-widen"
, "-Ywarn-unused"
)
