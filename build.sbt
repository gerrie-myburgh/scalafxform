val scala3Version = "3.0.0-RC3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scalafxforms",
    version := "0.0.0",

    scalaVersion := scala3Version,

    libraryDependencies += "javax.validation" % "validation-api" % "2.0.1.Final",
    libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.2",
    libraryDependencies += "org.json" % "json" % "20210307",
    libraryDependencies += "com.miglayout" % "miglayout-javafx" % "11.0",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.0-alpha1",
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "2.0.0-alpha1",
 
  )

  unmanagedBase := baseDirectory.value / "lib"
