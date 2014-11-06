val ElementNexus     = "Element Nexus"     at "http://repo.element.hr/nexus/content/groups/public/"
val ElementReleases  = "Element Releases"  at "http://repo.element.hr/nexus/content/repositories/releases/"
val ElementSnapshots = "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"


// ### BASIC SETTINGS ### //

organization := "io.jvm"

name := "jvm-privatization"

version := "0.0.1"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)

unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)


// ### DEPENDENCIES ### //

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4.2" % "test"
, "commons-io" % "commons-io" % "2.4" % "test"
)
// ### RESOLVERS ### //

resolvers := Seq(ElementNexus)

externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

publishTo := Some(
  if (version.value endsWith "-SNAPSHOT") ElementSnapshots else ElementReleases
)

credentials ++= {
  val creds = Path.userHome / ".config" / name.value / "nexus.config"
  if (creds.exists) Some(Credentials(creds)) else None
}.toSeq

publishArtifact in (Compile, packageDoc) := false


// ### COMPILE SETTINGS ### //

crossScalaVersions := Seq("2.11.4")

scalaVersion := crossScalaVersions.value.head

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
